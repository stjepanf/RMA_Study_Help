package com.mindorks.framework.myapplication.activities

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mindorks.framework.myapplication.*
import com.mindorks.framework.myapplication.models.College
import com.mindorks.framework.myapplication.models.Copier
import com.mindorks.framework.myapplication.models.Library
import com.mindorks.framework.myapplication.models.StudentRestaurant
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_add_new_location.*
import android.app.Activity

import android.R.attr.data
import android.util.Patterns
import java.util.regex.Pattern


const val MAP_ACTIVITY = 1
const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
const val URL_PATTERN = "[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)"

class AddNewLocationActivity : AppCompatActivity() {

    private lateinit var validationMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_location)

        openMap.setOnClickListener {
            loader.visibility = View.VISIBLE
            val addressIntent = Intent(this, MapsActivity::class.java)
            addressIntent.putExtra(SELECT_ADDRESS, true)
            startActivityForResult(addressIntent, MAP_ACTIVITY)
        }

        ivBack.setOnClickListener { finish() }

        checkWhichFieldsShouldBePresented()

        //after changing type of location
        rgButtons.setOnCheckedChangeListener { radioGroup, i ->
            checkWhichFieldsShouldBePresented()
        }

        tvSaveLocation.setOnClickListener {
            if (validateInputs()) {
                val realm: Realm = Realm.getDefaultInstance()
                realm.beginTransaction()

                if (rbCollege.isChecked) {
                    var id = 0
                    val lastItem = realm.where(College::class.java).findAll()
                    if (lastItem.isNotEmpty())
                        id = lastItem.last()!!.id + 1
                    val item = College(
                        id,
                        etName.text.toString(),
                        etAddress.text.toString(),
                        etTelephone.text.toString(),
                        etEmail.text.toString(),
                        etUrl.text.toString()
                    )
                    realm.copyToRealm(item)
                }
                if (rbLibrary.isChecked) {
                    var id = 0
                    val lastItem = realm.where(Library::class.java).findAll()
                    if (lastItem.isNotEmpty())
                        id = lastItem.last()!!.id + 1

                    val item = Library(
                        id,
                        etName.text.toString(),
                        etAddress.text.toString(),
                        etTelephone.text.toString(),
                        etEmail.text.toString(),
                        etUrl.text.toString()
                    )
                    realm.copyToRealm(item)

                }
                if (rbStudentRestaurant.isChecked) {
                    var id = 0
                    val lastItem = realm.where(StudentRestaurant::class.java).findAll()
                    if (lastItem.isNotEmpty())
                        id = lastItem.last()!!.id + 1

                    val item = StudentRestaurant(
                        id,
                        etName.text.toString(),
                        etAddress.text.toString(),
                        etTelephone.text.toString(),
                        etWorkInfo.text.toString(),
                    )
                    realm.copyToRealm(item)
                }

                if (rbCopier.isChecked) {
                    var id = 0
                    val lastItem = realm.where(StudentRestaurant::class.java).findAll()
                    if (lastItem.isNotEmpty())
                        id = lastItem.last()!!.id + 1

                    val item = Copier(
                        id,
                        etName.text.toString(),
                        etAddress.text.toString(),
                        etTelephone.text.toString(),
                        etEmail.text.toString(),
                        etWorkInfo.text.toString()
                    )
                    realm.copyToRealm(item)
                }
                realm.commitTransaction()
                realm.close()
                Toast.makeText(this, "Lokacija dodana!", Toast.LENGTH_SHORT).show()
                finish()
            } else
                Toast.makeText(this, validationMessage, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        loader.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MAP_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                val result = data!!.getStringExtra(EXTRA_ADDRESS)
                etAddress.setText(result)
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isAllClear = true
        var validEmail = true
        var validUrl = true
        validationMessage = "Molimo unesite sve tražene podatke"
        if (etName.text.isEmpty() || etAddress.text.isEmpty() || etTelephone.text.isEmpty()) {
            isAllClear = false
        }
        if (rbCollege.isChecked)
            if (etEmail.text.isEmpty() || etUrl.text.isEmpty()) {
                isAllClear = false
            }

        if (rbLibrary.isChecked)
            if (etEmail.text.isEmpty() || etWorkInfo.text.isEmpty()) {
                isAllClear = false
            }

        if (rbStudentRestaurant.isChecked)
            if (etWorkInfo.text.isEmpty()) {
                isAllClear = false
            }

        if (rbCopier.isChecked)
            if (etEmail.text.isEmpty() || etWorkInfo.text.isEmpty()) {
                isAllClear = false
            }
        if (rbCollege.isChecked || rbLibrary.isChecked || rbCopier.isChecked) {
            if (etEmail.text.isNotEmpty())
                if (!Pattern.compile(EMAIL_PATTERN).matcher(etEmail.text.toString()).matches()) {
                    isAllClear = false
                    validEmail = false
                }
        }

        if (rbCollege.isChecked){
            if (etUrl.text.isNotEmpty())
                if (!Pattern.compile(URL_PATTERN).matcher(etUrl.text.toString()).matches()) {
                    isAllClear = false
                    validUrl = false
                }
        }
        if (!validEmail){
            validationMessage = "Unesite točnu email adresu!"
        }
        if (!validUrl){
            validationMessage = "Unesite točnu web adresu!"
        }
        if (!validEmail && !validUrl){
            validationMessage = "Unesite točnu web i email adresu!"
        }

        return isAllClear
    }

    private fun checkWhichFieldsShouldBePresented() {
        if (rbCollege.isChecked) {
            etEmail.visibility = View.VISIBLE
            etUrl.visibility = View.VISIBLE
            etWorkInfo.visibility = View.GONE
        }
        if (rbLibrary.isChecked) {
            etEmail.visibility = View.VISIBLE
            etUrl.visibility = View.GONE
            etWorkInfo.visibility = View.VISIBLE
        }
        if (rbStudentRestaurant.isChecked) {
            etEmail.visibility = View.GONE
            etUrl.visibility = View.GONE
            etWorkInfo.visibility = View.VISIBLE
        }

        if (rbCopier.isChecked) {
            etEmail.visibility = View.VISIBLE
            etUrl.visibility = View.GONE
            etWorkInfo.visibility = View.VISIBLE
        }
    }
}