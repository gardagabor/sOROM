package hu.bme.aut.android.srm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.model.DocumentKey
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.srm.adapter.MyRecipiesAdapter
import hu.bme.aut.android.srm.databinding.ActivityMainBinding
import hu.bme.aut.android.srm.model.*
import androidx.work.Worker
import androidx.work.WorkerParameters
import hu.bme.aut.android.srm.notification.ReminderBroadcast


class RecipeListActivity : AppCompatActivity(),
    MyRecipiesAdapter.BeerRecipeItemClickListener,
RecipeCreateFragment.RecipeCreatedListener,
RecipeUpdateFragment.RecipeUpdatedListener{

    private lateinit var binding: ActivityMainBinding

    private lateinit var myRecipiesAdapter: MyRecipiesAdapter

    private var myRecipiesDocumentReferences = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = title

        setupRecyclerView()

        initPostsListener()

        createNotificationChannel()

    }

    private fun setupRecyclerView() {

        myRecipiesAdapter = MyRecipiesAdapter()
        myRecipiesAdapter.itemClickListener = this
      //fillListWithDefValues()
        binding.root.findViewById<RecyclerView>(R.id.recipe_list).adapter = myRecipiesAdapter
    }

    override fun onItemClick(beerRecipe: BeerRecipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("BeerRecipe", beerRecipe);
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, view: View, recipe: BeerRecipe): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_recipe)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {

                    myRecipiesAdapter.deleteElement(recipe)

                    var db = Firebase.firestore

                    db.collection("recipes").document(myRecipiesDocumentReferences.get(position)).delete()
                        .addOnSuccessListener { Log.d("Delete_tag", "DocumentSnapshot successfully deleted!") }
                        .addOnFailureListener { e -> Log.w("Delete_tag", "Error deleting document", e) }

                    myRecipiesDocumentReferences.removeAt(position)

                }

                R.id.modify -> {
                    val todoUpdateFragment = RecipeUpdateFragment(recipe)
                    todoUpdateFragment.show(supportFragmentManager, "TAG")
                }
                R.id.dry_hop ->{

                    Toast.makeText(this,"Reminder Set!",Toast.LENGTH_SHORT).show()

                    var intent = Intent(this,ReminderBroadcast::class.java)
                    var pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)

                    var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

                    var currentTime = System.currentTimeMillis()

                    var oneWeek = 10 * 1000

                    alarmManager.set(AlarmManager.RTC_WAKEUP,currentTime+oneWeek,pendingIntent)
                }
        }
            false
        }
        popup.show()
        return false
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "sÖRÖM"
            val descriptionText = "sÖRÖM Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("sÖRÖM", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemCreateRecipe -> {
                val recipeCreateFragment = RecipeCreateFragment()
                recipeCreateFragment.show(supportFragmentManager, "TAG")
            }
            R.id.itemSearchRecipe -> {
                val intent = Intent(this, RecipeSearchActivity::class.java)
                startActivityForResult(intent, 0)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeCreated(beerRecipe: BeerRecipe) {
        val db = Firebase.firestore

        db.collection("recipes")
            .add(beerRecipe)
            .addOnSuccessListener {
                toast("Recipe created")
            }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            val passedItem: ArrayList<BeerRecipe>? = data?.extras!!["new_added_recipes"] as ArrayList<BeerRecipe>?
            val db = Firebase.firestore
            passedItem?.forEach {
                db.collection("recipes")
                    .add(it)
                    .addOnFailureListener { e -> toast(e.toString()) }
            }
        }
    }

    override fun onRecipeUpdated(oldRecipe: BeerRecipe, newRecipe: BeerRecipe){
        var position = myRecipiesAdapter.getBeerRecipeList().indexOf(oldRecipe)

        myRecipiesAdapter.deleteElement(oldRecipe)

        var db = Firebase.firestore

        db.collection("recipes").document(myRecipiesDocumentReferences.get(position)).delete()
            .addOnSuccessListener { Log.d("Delete_tag", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("Delete_tag", "Error deleting document", e) }

        myRecipiesDocumentReferences.removeAt(position)



        db.collection("recipes")
            .add(newRecipe)
            .addOnSuccessListener {
                toast("Recipe updated")
            }
            .addOnFailureListener { e -> toast(e.toString()) }

    }

    private fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initPostsListener() {
        val db = Firebase.firestore
        db.collection("recipes")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            myRecipiesDocumentReferences.add(myRecipiesDocumentReferences.size,dc.document.id)
                            myRecipiesAdapter.addItem(dc.document.toObject<BeerRecipe>())
                        }
                    }
                }
            }
    }

}