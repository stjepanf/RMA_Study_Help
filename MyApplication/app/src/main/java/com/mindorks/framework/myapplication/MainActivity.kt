package com.mindorks.framework.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.mindorks.framework.myapplication.activities.AddNewLocationActivity
import com.mindorks.framework.myapplication.activities.ItemDetailsActivity
import com.mindorks.framework.myapplication.models.*
import com.mindorks.framework.myapplication.utils.createLocalDatabase
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var listViewAdapter: ExpandableListViewAdapter
    private lateinit var chapterList: List<String>
    private lateinit var topicList: HashMap<String, List<ListItemData>>
    private lateinit var realm: Realm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //realm initialization
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        //checking is app first run for user
        checkIsFirstRunAndCreateDatabase()
        showList()
        listViewAdapter = ExpandableListViewAdapter(this, chapterList, topicList)
        eListView.setAdapter(listViewAdapter)
        eListView.setOnItemLongClickListener { adapterView, view, i, id ->
            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                val groupPosition = ExpandableListView.getPackedPositionGroup(id)
                val childPosition = ExpandableListView.getPackedPositionChild(id)

                deleteItemFromDatabase(
                    (listViewAdapter.getChild(
                        groupPosition,
                        childPosition
                    ) as ListItemData).id,
                    (listViewAdapter.getChild(groupPosition, childPosition) as ListItemData).type
                )

            }
            true
        }
        eListView.setOnChildClickListener(object : ExpandableListView.OnChildClickListener {
            override fun onChildClick(
                p0: ExpandableListView?,
                p1: View?,
                p2: Int,
                p3: Int,
                p4: Long
            ): Boolean {
                //open activity to show details of list element
                val openItemDetailsActivity =
                    Intent(this@MainActivity, ItemDetailsActivity::class.java)
                openItemDetailsActivity.putExtra(
                    EXTRA_ID,
                    (listViewAdapter.getChild(p2, p3) as ListItemData).id
                )
                openItemDetailsActivity.putExtra(
                    EXTRA_TYPE,
                    (listViewAdapter.getChild(p2, p3) as ListItemData).type
                )
                startActivity(openItemDetailsActivity)
                return true
            }
        })
    }

    private fun deleteItemFromDatabase(itemId: Int, type: String) {

        when (type) {
            COLLEGE -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.executeTransactionAsync { realm ->
                    val result: RealmResults<College> =
                        realm.where(College::class.java).equalTo("id", itemId).findAll()
                    result.deleteAllFromRealm()
                }
            }
            LIBRARY -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.executeTransactionAsync { realm ->
                    val result: RealmResults<Library> =
                        realm.where(Library::class.java).equalTo("id", itemId).findAll()
                    result.deleteAllFromRealm()
                }
            }
            STUDENT_RESTAURANT -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.executeTransactionAsync { realm ->
                    val result: RealmResults<StudentRestaurant> =
                        realm.where(StudentRestaurant::class.java).equalTo("id", itemId).findAll()
                    result.deleteAllFromRealm()
                }

            }
            COPIER -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.executeTransactionAsync { realm ->
                    val result: RealmResults<Copier> =
                        realm.where(Copier::class.java).equalTo("id", itemId).findAll()
                    result.deleteAllFromRealm()
                }
            }
        }

        realm.close()

        refreshList()
    }

    private fun refreshList() {
        showList()
        listViewAdapter = ExpandableListViewAdapter(this, chapterList, topicList)
        eListView.setAdapter(listViewAdapter)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun checkIsFirstRunAndCreateDatabase() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        if (!sharedPreferences.getBoolean(IS_FIRST_RUN, false)) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(IS_FIRST_RUN, true)
            editor.apply()
            createLocalDatabase()
        }
    }

    private fun showList() {
        chapterList = ArrayList()
        topicList = HashMap()

        (chapterList as ArrayList<String>).add("College/Fakultet")
        (chapterList as ArrayList<String>).add("Llibrary/Knjižnica")
        (chapterList as ArrayList<String>).add("Student Restaurant/Menza")
        (chapterList as ArrayList<String>).add("Copier/Skripta")


        val topic1: ArrayList<ListItemData> = getCollegeNames()

        val topic2: MutableList<ListItemData> = getLibraryNames()

        val topic3: MutableList<ListItemData> = getStudentRestaurantsNames()

        val topic4: MutableList<ListItemData> = getCopierNames()

        topicList[chapterList[0]] = topic1
        topicList[chapterList[1]] = topic2
        topicList[chapterList[2]] = topic3
        topicList[chapterList[3]] = topic4

    }

    //method for getting college data from database and preparing data for presenting
    private fun getCollegeNames(): ArrayList<ListItemData> {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance()
        }
        realm.beginTransaction()
        val colleges: RealmResults<College> = realm.where(College::class.java).findAll()
        realm.commitTransaction()
        val list = ArrayList<ListItemData>()
        colleges.forEach {
            list.add(ListItemData(it.id, it.name, COLLEGE))
        }
        return list
    }

    //method for getting library data from database and preparing data for presenting
    private fun getLibraryNames(): ArrayList<ListItemData> {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance()
        }
        realm.beginTransaction()
        val libraries: RealmResults<Library> = realm.where(Library::class.java).findAll()
        realm.commitTransaction()
        val list = ArrayList<ListItemData>()
        libraries.forEach {
            list.add(ListItemData(it.id, it.name, LIBRARY))
        }
        return list
    }

    //method for getting student restaurants data from database and preparing data for presenting
    private fun getStudentRestaurantsNames(): MutableList<ListItemData> {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance()
        }
        realm.beginTransaction()
        val restaurants: RealmResults<StudentRestaurant> =
            realm.where(StudentRestaurant::class.java).findAll()
        realm.commitTransaction()
        val list = ArrayList<ListItemData>()
        restaurants.forEach {
            list.add(ListItemData(it.id, it.name, STUDENT_RESTAURANT))
        }
        return list
    }

    //method for getting copier data from database and preparing data for presenting
    private fun getCopierNames(): MutableList<ListItemData> {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance()
        }
        realm.beginTransaction()
        val copiers: RealmResults<Copier> = realm.where(Copier::class.java).findAll()
        realm.commitTransaction()
        val list = ArrayList<ListItemData>()
        copiers.forEach {
            list.add(ListItemData(it.id, it.name, COPIER))
        }
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addLocation -> {
                val addLocationIntent =
                    Intent(this@MainActivity, AddNewLocationActivity::class.java)
                startActivity(addLocationIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}