package com.mindorks.framework.myapplication.activities

import android.os.Bundle
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
import android.graphics.Paint
import android.net.Uri
import android.text.Html
import androidx.appcompat.app.AppCompatActivity


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
        tvEmail.paintFlags = tvEmail.paintFlags or Paint.UNDERLINE_TEXT_FLAG
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
            loader.visibility = View.VISIBLE
            val addressIntent = Intent(this, MapsActivity::class.java)
            addressIntent.putExtra(EXTRA_TITLE, name)
            addressIntent.putExtra(EXTRA_ADDRESS, address)
            addressIntent.putExtra(SHOW_MARKER, true)
            startActivity(addressIntent)
        }

        tvEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "plain/text"
            emailIntent.data = Uri.parse("mailto:$email")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            startActivity(Intent.createChooser(emailIntent, "Pošalji email"))
        }

        tel.setOnClickListener {
                val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telephone"))
                startActivity(phoneIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        loader.visibility = View.GONE
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