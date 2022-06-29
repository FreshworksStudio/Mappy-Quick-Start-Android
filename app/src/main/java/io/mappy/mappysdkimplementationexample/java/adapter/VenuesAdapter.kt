package io.mappy.mappysdkimplementationexample.java.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.bemappy.sdk.models.Venue
import io.mappy.mappysdkimplementationexample.databinding.VenueViewBinding
import io.mappy.mappysdkimplementationexample.java.activities.MapActivity

class VenuesAdapter: RecyclerView.Adapter<VenuesAdapter.VenueViewHolder>() {

    private val venues = arrayListOf<Venue>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VenueViewHolder(
        VenueViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        if (venues.isNotEmpty() && position in 0 until venues.count()) {
            holder.load(venues[position])
        }
    }

    override fun getItemCount(): Int = venues.count()

    fun load(venue: List<Venue>) {
        this.venues.apply {
            clear()
            addAll(venue)
        }
        notifyItemRangeChanged(0, this.venues.count())
    }

    inner class VenueViewHolder(
        private val binding: VenueViewBinding
        ): RecyclerView.ViewHolder(binding.root) {

        fun load(venue: Venue) {
            binding.textViewTitle.text = venue.data.name
            binding.textViewDescription.text = venue.data.description
            binding.buttonGoToMap.setOnClickListener {
                val context = binding.root.context
                context.startActivity(Intent(context, MapActivity::class.java).apply {
                    putExtra("venue", venue)
                })
            }
        }
    }

}