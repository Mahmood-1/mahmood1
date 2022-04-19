package com.iug.books

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.iug.books.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(applicationContext, AddActivity::class.java);
            startActivity(intent)
        }


    }

    override fun onResume() {

        val db = FirebaseFirestore.getInstance()

        db.collection("Books")
            .get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {

                    binding.rvData.layoutManager = LinearLayoutManager(applicationContext)
                    val data = ArrayList<Book>()

                    for (document in task.result!!) {
                        data.add(
                            Book(
                                document.id,
                                document.data["name"].toString(),
                                document.data["auth"].toString(),
                                document.data["date"].toString().toLong(),
                                document.data["price"].toString().toInt(),
                                document.data["rate"].toString()
                            )
                        )
                    }

                    val adapter = MyAdapter(data, applicationContext)

                    binding.rvData.adapter = adapter

                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            })

        super.onResume()
    }
}