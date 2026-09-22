package leite.sampaio.lucas.roomsqlite.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import leite.sampaio.lucas.roomsqlite.databinding.BookItemBinding
import leite.sampaio.lucas.roomsqlite.entities.Book

class BookAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onDeleteClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var books = emptyList<Book>()

    class BookViewHolder(private val binding: BookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book, onItemClick: (Book) -> Unit, onDeleteClick: (Book) -> Unit) {
            binding.tvTitle.text = book.title
            binding.tvAuthor.text = binding.root.context.getString(
                leite.sampaio.lucas.roomsqlite.R.string.label_author_format, book.author
            )
            binding.tvYear.text = binding.root.context.getString(
                leite.sampaio.lucas.roomsqlite.R.string.label_year_format, book.year.toString()
            )
            binding.tvPublisher.text = binding.root.context.getString(
                leite.sampaio.lucas.roomsqlite.R.string.label_publisher_format, book.publisher
            )

            binding.root.setOnClickListener {
                onItemClick(book)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(book)
            }
        }
    }

    // DiffUtil.ItemCallback define como comparar itens antigos e novos
    object BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        // Verifica se os itens representam o mesmo objeto (mesma identidade)
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        // Verifica se o conteúdo do item é idêntico (usa equals() do data class)
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position], onItemClick, onDeleteClick)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun updateList(novosLivros: List<Book>) {
        // Calcula as diferenças entre a lista atual e a nova
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = books.size
            override fun getNewListSize(): Int = novosLivros.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return books[oldItemPosition].id == novosLivros[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return books[oldItemPosition] == novosLivros[newItemPosition]
            }
        })

        books = novosLivros
        // Aplica apenas as mudanças identificadas pelo DiffUtil (sem recarregar tudo)
        diffResult.dispatchUpdatesTo(this)
    }
}
