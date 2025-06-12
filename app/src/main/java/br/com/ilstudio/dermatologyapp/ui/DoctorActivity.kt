package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.adapter.DoctorsAdapter
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryDoctors
import br.com.ilstudio.dermatologyapp.databinding.ActivityDoctorBinding
import kotlinx.coroutines.launch

class DoctorActivity : AppCompatActivity() {
    enum class FilterType { A_TO_Z, Z_TO_A, MALE, FEMALE, FAVORITE }

    private lateinit var binding: ActivityDoctorBinding
    private lateinit var firestoreRepositoryDoctors: FirestoreRepositoryDoctors
    private var filters = mutableSetOf<FilterType>(FilterType.A_TO_Z)
    private var data: List<DoctorsData> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreRepositoryDoctors = FirestoreRepositoryDoctors()

        lifecycleScope.launch {
            getAllDoctorsData()
        }

        binding.header.setOnBackButtonClickListener { finish() }
        binding.aToZFilter.setOnClickListener { toggleSorting() }
        binding.favoriteFilter.setOnClickListener { toggleFilter(FilterType.FAVORITE, binding.favoriteFilter) }
        binding.maleFilter.setOnClickListener { toggleFilter(FilterType.MALE, binding.maleFilter) }
        binding.femaleFilter.setOnClickListener { toggleFilter(FilterType.FEMALE, binding.femaleFilter) }
    }

    private suspend fun getAllDoctorsData() {
        val response = firestoreRepositoryDoctors.getAll()

        if (!response.success) {
            binding.textError.text = response.message
            binding.recycle.visibility = View.GONE
            binding.error.visibility = View.VISIBLE
            return
        }

        if (response.isEmpty) {
            binding.recycle.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
            return
        }

        data = response.data ?: listOf()
        updateDoctorList()
    }

    private fun toggleSorting() {
        if (FilterType.A_TO_Z in filters) {
            filters.remove(FilterType.A_TO_Z)
            filters.add(FilterType.Z_TO_A)
            binding.textA.text = "Z"
            binding.textZ.text = "A"
        } else {
            filters.remove(FilterType.Z_TO_A)
            filters.add(FilterType.A_TO_Z)
            binding.textA.text = "A"
            binding.textZ.text = "Z"
        }
        updateDoctorList()
    }

    private fun toggleFilter(filter: FilterType, view: View) {
        if (filter == FilterType.MALE) {
            filters.remove(FilterType.FEMALE)
            updateFilterButton(binding.femaleFilter, false)
        } else if (filter == FilterType.FEMALE) {
            filters.remove(FilterType.MALE)
            updateFilterButton(binding.maleFilter, false)
        }

        if (!filters.add(filter)) {
            filters.remove(filter)
        }

        updateFilterButton(view, filter in filters)
        updateDoctorList()
    }

    private fun updateFilterButton(view: View, isActive: Boolean) {
        val color = if (isActive) R.color.white else R.color.primary
        val background = if (isActive) R.color.primary else R.color.blue_light
        view.backgroundTintList = ColorStateList.valueOf(getColor(background))

        if (view is ImageView) {
            view.imageTintList = ColorStateList.valueOf(getColor(color))
        }
    }

    private fun updateDoctorList() {
        var filteredData = data

        if (FilterType.MALE in filters) filteredData = filteredData.filter { it.type == "male" }
        if (FilterType.FEMALE in filters) filteredData = filteredData.filter { it.type == "female" }
        if (FilterType.FAVORITE in filters) filteredData = filteredData.filter { it.favorite }

        when {
            FilterType.A_TO_Z in filters -> filteredData = filteredData.sortedBy { it.name }
            FilterType.Z_TO_A in filters -> filteredData = filteredData.sortedByDescending { it.name }
        }

        binding.recycle.layoutManager = LinearLayoutManager(this)
        binding.recycle.adapter = DoctorsAdapter(
            filteredData,
            onFavItemClick = { doctor ->
                lifecycleScope.launch {
                    firestoreRepositoryDoctors.updateFavoriteDoctor(doctor.id, !doctor.favorite)
                }
            },
            onItemClick = { doctor ->
                    val intent = Intent(this, DoctorInfoActivity::class.java)
                    intent.putExtra("doctor", doctor.toString())
                    startActivity(intent)
            }
        )
    }
}
