package com.mindorks.framework.myapplication.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mindorks.framework.myapplication.*
import com.mindorks.framework.myapplication.models.*
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_item_details.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var telephone: String
    private lateinit var email: String
    private lateinit var url: String
    private lateinit var workTimeInfo: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        //get id and type so we can get correct data from database
        val id = intent.getIntExtra(EXTRA_ID, INIT_INT)
        val type = intent.getStringExtra(EXTRA_TYPE)!!

        ivBack.setOnClickListener { finish() }
        getDataFromDatabaseByIdAndType(type, id)

        //setting data to textview
        tvTitle.text = name
        tvAddress.text = address
        tvTelephone.text = telephone

        if (email.isEmpty())
            tvEmail.visibility = View.GONE
        else
            tvEmail.visibility = View.VISIBLE
        tvEmail.text = email

        if (url.isEmpty())
            tvURL.visibility = View.GONE
        else
            tvURL.visibility = View.VISIBLE
        tvURL.text = url

        if (workTimeInfo.isEmpty())
            tvWorkTimeInfo.visibility = View.GONE
        else
            tvWorkTimeInfo.visibility = View.VISIBLE
        tvWorkTimeInfo.text = workTimeInfo

        maps.setOnClickListener {
            val mapUri: Uri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        tel.setOnClickListener {
                val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telephone"))
                startActivity(phoneIntent)
        }
    }

    //get data from realm by type and id
    private fun getDataFromDatabaseByIdAndType(type: String, id: Int) {
        realm = Realm.getDefaultInstance()
        when (type) {
            COLLEGE -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.beginTransaction()
                val college: College? = realm.where(College::class.java).equalTo("id", id).findFirst()
                realm.commitTransaction()
                name = college?.name ?: EMPTY_STRING
                address = college?.address ?: EMPTY_STRING
                telephone = college?.telephone ?: EMPTY_STRING
                email = college?.email ?: EMPTY_STRING
                url = college?.url ?: EMPTY_STRING
                workTimeInfo = EMPTY_STRING
            }
            LIBRARY -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.beginTransaction()
                val library: Library? =
                    realm.where(Library::class.java).equalTo("id", id).findFirst()
                realm.commitTransaction()
                name = library?.name ?: EMPTY_STRING
                address = library?.address ?: EMPTY_STRING
                telephone = library?.telephone ?: EMPTY_STRING
                email = library?.email ?: EMPTY_STRING
                url = library?.url ?: EMPTY_STRING
                workTimeInfo = EMPTY_STRING
            }
            STUDENT_RESTAURANT -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.beginTransaction()
                val studentRestaurant: StudentRestaurant? =
                    realm.where(StudentRestaurant::class.java).equalTo("id", id).findFirst()
                realm.commitTransaction()
                name = studentRestaurant?.name ?: EMPTY_STRING
                address = studentRestaurant?.address ?: EMPTY_STRING
                telephone = studentRestaurant?.telephone ?: EMPTY_STRING
                email = EMPTY_STRING
                url = EMPTY_STRING
                workTimeInfo = studentRestaurant?.workTimeInfo ?: EMPTY_STRING
            }
            COPIER -> {
                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance()
                }
                realm.beginTransaction()
                val copier: Copier? =
                    realm.where(Copier::class.java).equalTo("id", id).findFirst()
                realm.commitTransaction()
                name = copier?.name ?: EMPTY_STRING
                address = copier?.address ?: EMPTY_STRING
                telephone = copier?.telephone ?: EMPTY_STRING
                email =  copier?.email ?:  EMPTY_STRING
                url = EMPTY_STRING
                workTimeInfo = copier?.workTimeInfo ?: EMPTY_STRING
            }
        }
    }
}