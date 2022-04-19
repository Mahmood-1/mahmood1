package com.iug.books

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.iug.books.databinding.ItemListBinding
import java.text.SimpleDateFormat
import java.util.*

class MyAdapter(private val data: List<Book>, val context: Context) :
    RecyclerView.Adapter<MyAdapter.BookHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val itemBinding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookHolder(itemBinding, context)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val book: Book = data[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int = data.size

    class BookHolder(private val itemBinding: ItemListBinding, val context: Context) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(book: Book) {
            itemBinding.tvName.text = book.name
            itemBinding.tvAuth.text = book.auth
            itemBinding.tvPrice.text = "${book.price}"
            itemBinding.tvDate.text = convertLongToTime(book.date)

            itemBinding.button.setOnClickListener {
                val intent = Intent(context.applicationContext, DeleteAndEditActivity::class.java);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", book.name)
                intent.putExtra("price", book.price)
                intent.putExtra("auth", book.auth)
                intent.putExtra("date", book.date)
                intent.putExtra("rate", book.rate)
                intent.putExtra("id", book.id)
                context.startActivity(intent)
            }
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            return sdf.format(date)
        }

    }

}