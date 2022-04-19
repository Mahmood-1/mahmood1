package com.iug.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.iug.books.databinding.ActivityDeleteAndEditBinding
import com.iug.books.databinding.ActivityMainBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DeleteAndEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityDeleteAndEditBinding
    var timeInMilliseconds: Long = 0
    lateinit var db: FirebaseFirestore
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAndEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = FirebaseFirestore.getInstance()

        id = intent.getStringExtra("id")!!
        val name = intent.getStringExtra("name")
        val auth = intent.getStringExtra("auth")
        val price = intent.getIntExtra("price", 0)
        val date = intent.getLongExtra("date", 0)
        val rate = intent.getStringExtra("rate")

        binding.editName.setText(name)
        binding.editAuth.setText(auth)
        binding.editPrice.setText("$price")
        binding.editYear.setText(convertLongToTime(date))
        binding.rate.rating = rate!!.toFloat()

        binding.delete.setOnClickListener {
            db.collection("Books").document(id).delete().addOnSuccessListener { documentReference ->
                finish()
            }
                .addOnFailureListener { e -> Log.e("TAG", "Error adding document", e) }
        }

        binding.edit.setOnClickListener {
            dateformat(binding.editYear.text.toString())

            val map: MutableMap<String, Any> = HashMap()
            map["name"] = binding.editName.text.toString()
            map["auth"] = binding.editAuth.text.toString()
            map["date"] = timeInMilliseconds
            map["price"] = binding.editPrice.text.toString().toInt()
            map["rate"] = binding.rate.rating.toDouble()

            db.collection("Books").document(id).update(map).addOnSuccessListener { documentReference ->
                finish()
            }
                .addOnFailureListener { e -> Log.e("TAG", "Error adding document", e) }
        }

    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return sdf.format(date)
    }

    fun dateformat(dt: String) {
        binding.editYear.setText(dt)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            val mDate = sdf.parse(dt)
            timeInMilliseconds = mDate!!.time
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

}