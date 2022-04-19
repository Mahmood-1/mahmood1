package com.iug.books

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.iug.books.databinding.ActivityAddBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AddActivity : AppCompatActivity() {

    val TAG = "AddActivity"
    lateinit var binding: ActivityAddBinding
    var timeInMilliseconds: Long = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addYear.setOnClickListener {
            clickDataPicker()
        }

        binding.addButton.setOnClickListener {
            if (binding.addName.text.isNotEmpty() && binding.addAuth.text.isNotEmpty() &&
                binding.addPrice.text.isNotEmpty() && binding.addName.text.isNotEmpty()
            ) {

                val map: MutableMap<String, Any> = HashMap()
                map["name"] = binding.addName.text.toString()
                map["auth"] = binding.addAuth.text.toString()
                map["date"] = timeInMilliseconds
                map["price"] = binding.addPrice.text.toString().toInt()
                map["rate"] = binding.addRate.rating.toDouble()

                val db = FirebaseFirestore.getInstance()

                db.collection("Books")
                    .add(map)
                    .addOnSuccessListener { documentReference ->
                        finish()
                    }
                    .addOnFailureListener { e -> Log.e(TAG, "Error adding document", e) }

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickDataPicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
            dateformat("$year-${monthOfYear + 1}-$dayOfMonth")

        }, year, month, day)
        dpd.show()
    }

    fun dateformat(dt: String) {
        binding.addYear.setText(dt)
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