package com.mindorks.framework.myapplication.utils

import com.mindorks.framework.myapplication.models.College
import com.mindorks.framework.myapplication.models.Copier
import com.mindorks.framework.myapplication.models.Library
import com.mindorks.framework.myapplication.models.StudentRestaurant
import io.realm.Realm

fun createLocalDatabase() {
    val realm: Realm = Realm.getDefaultInstance()
    realm.beginTransaction()

    //college
    val ferit = College(0, "FERIT","Kneza Trpimira 2B, 31000 Osijek", " +385-31-224-600", "ferit@ferit.hr", " https://www.ferit.unios.hr/")
    val efos = College(1, "Ekonomski fakultet","Trg Ljudevita Gaja 7, 31000 Osijek", "+385-31-224-400", "ured@efos.hr", "http://www.efos.hr")
    val fazos = College(2, "FAZOS","Vladimira Preloga 1, 31000 Osijek", "+385-31-554-844", "web@fazos.hr", "http://www.fazos.unios.hr/hr/")
    val pravos = College(3, "Pravni fakultet","Radićeva 13, 31000 Osijek", "+385-31-224-500", "ured@pravos.hr", " http://www.pravos.hr")
    val ufos = College(4, "Umjetnička akademija","Kralja Petra Svačića 1/F, 31000 Osijek", "+385 31 253 333", "uaos@uaos.hr", "http:///www.uaos.unios.hr")
    val ffos = College(5, "Filozofski fakultet","Lorenza Jägera 9, 31000 Osijek", "+385-31-211-400", "helpdesk@knjiga.ffos.hr", " http://www.ffos.hr")
    val ptfos = College(6, "Prehrambeno-tehnološki fakultet Osijek","Franje Kuhača 18, 31000 Osijek", "+385-31-224-300", "office@ptfos.hr", " www.ptfos.hr")
    val mefos = College(7, "Medicinski fakultet Osijek","Josipa Huttlera 4, 31000 Osijek", "+385-31-512-801", "medicina@mefos.hr", "http://www.mefos.hr/")

    realm.copyToRealm(ferit)
    realm.copyToRealm(efos)
    realm.copyToRealm(fazos)
    realm.copyToRealm(pravos)
    realm.copyToRealm(ufos)
    realm.copyToRealm(ffos)
    realm.copyToRealm(ptfos)
    realm.copyToRealm(mefos)

    // studentRestaurants
    val istarska = StudentRestaurant(0, "Restoran Istarska", " Istarska 5, Osijek", "+385-31-220-600", "– ponedjeljak – petak: doručak, ručak, večera od 08:00 do 19:30 sati\n" +
            "– subota – nedjelja: ručak i večera od 11:30 do 19:00 sati")
    val gaudeaumus = StudentRestaurant(1, "Restoran Gaudeamus", "Istarska 5, Osijek", "+385-31-220-600", "– ponedjeljak-petak od 11:00 do 20:00 sati\n" +
            "– subota i nedjelja: zatvoreno")
    val campus = StudentRestaurant(2, "Restoran Campus", "Trg L. Ružičke 3, Osijek", "+385-31-251-346", "- ponedjeljak – petak: od 07:00 do 22:00\n" +
            "- subota i nedjelja: zatvoreno")
    val studentskiKlub = StudentRestaurant(3, "Caffe-bar Studentski klub", "Istarska 5, Osijek", "+385-31-220-600", "- ponedjeljak – petak: od 07:00 do 19:00\n" +
            "- subota : od 7:00 do 14:00\n" +
            "- nedjelja: zatvoreno")

    realm.copyToRealm(istarska)
    realm.copyToRealm(gaudeaumus)
    realm.copyToRealm(campus)
    realm.copyToRealm(studentskiKlub)

    // library
    val gskos = Library(0, "Gradska i sveučilišna knjižnica Osijek", "Europska avenija 24, 31000, Osijek", "+385- 31-211-218", "gisko@gskos.hr", " https://www.gskos.unios.hr/")

    realm.copyToRealm(gskos)
    // copier

    val dragun = Copier(0, "Dragun Copy", "Ul. Josipa Huttlera 27A, 31000, Osijek", "+385-98-182-5045", "dragun.copy@gmail.com", "– ponedjeljak-petak od 07:30 do 19:30 sati\n" +
            "– subota od 09:00 do 13:00 sati\n" +
            "- nedjelja: zatvoreno")
    val hitro = Copier(1, "HITROPRINT TVRĐA", "Ul. Franje Markovića 5, 31000, Osijek", "+385-95-564-1638", "hitroprint@gmail.com", "– ponedjeljak-petak od 07:00 do 17:30 sati\n" +
            "– subota i nedjelja: zatvoreno")
    val proPhoto = Copier(2, "Unknown ProPhoto", "Sjenjak 133, 31000, Osijek", "+385- 99-416-3390", "unknown.prophoto@gmail.com", "– ponedjeljak-petak od 07:30 do 20:0 sati\n" +
            "– subota od 08:00 do 13:00 sati\n" +
            "- nedjelja: zatvoreno")
    realm.copyToRealm(dragun)
    realm.copyToRealm(hitro)
    realm.copyToRealm(proPhoto)

    realm.commitTransaction()
    realm.close()
}