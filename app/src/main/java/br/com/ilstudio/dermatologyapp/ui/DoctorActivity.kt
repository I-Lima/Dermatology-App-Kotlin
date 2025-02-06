package br.com.ilstudio.dermatologyapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ilstudio.dermatologyapp.adapter.DoctorsAdapter
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryDoctors
import br.com.ilstudio.dermatologyapp.databinding.ActivityDoctorBinding
import kotlinx.coroutines.launch
import java.util.logging.Filter

enum class FilterType(val type: String) {
    A_TO_Z("AtoZ"),
    Z_TO_A("ZtoA"),
    MALE("male"),
    FEMALE("female"),
    FAVORITE("favorite")
}

class DoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorBinding
    private lateinit var firestoreRepositoryDoctors: FirestoreRepositoryDoctors
    private lateinit var orderType: FilterType
    private lateinit var data: List<DoctorsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderType = FilterType.A_TO_Z
        firestoreRepositoryDoctors = FirestoreRepositoryDoctors()

        lifecycleScope.launch {
            getAllDoctorsData()
        }

        binding.header.setOnBackButtonClickListener {
            finish()
        }

        binding.favoriteFilter.setOnClickListener {
            val orderedData = changeFilter(FilterType.FAVORITE, data)
            binding.recycle.adapter = DoctorsAdapter(orderedData)
        }
        binding.maleFilter.setOnClickListener {
            val orderedData = changeFilter(FilterType.MALE, data)
            binding.recycle.adapter = DoctorsAdapter(orderedData)
        }
        binding.femaleFilter.setOnClickListener {
            val orderedData = changeFilter(FilterType.FEMALE, data)
            binding.recycle.adapter = DoctorsAdapter(orderedData)
        }
    }

    private suspend fun getAllDoctorsData() {
        val response = firestoreRepositoryDoctors.getAll()

        if (!response.success) {
            binding.textError.text = response.message
            binding.recycle.visibility = View.GONE
            binding.error.visibility = View.VISIBLE
        }

        if (response.isEmpty) {
            binding.recycle.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }

        data = changeFilter(FilterType.A_TO_Z, response.data!!)

        binding.recycle.layoutManager = LinearLayoutManager(this)
        binding.recycle.adapter = DoctorsAdapter(data)
    }

    /**
     * Changes the order or filters a list of doctors based on the specified type.
     *
     * This function takes a sorting or filtering type and applies the corresponding operation
     * to the given list of [DoctorsData]. It supports sorting alphabetically (A-Z, Z-A) and
     * filtering by gender (male, female). If an unknown type is provided, it defaults to sorting
     * the list from A to Z.
     *
     * @param type The type of ordering or filtering to apply. Supported values:
     *  - `"AtoZ"`: Sorts doctors alphabetically in ascending order.
     *  - `"ZtoA"`: Sorts doctors alphabetically in descending order.
     *  - `"male"`: Filters the list to include only male doctors.
     *  - `"female"`: Filters the list to include only female doctors.
     *
     * @param data The list of [DoctorsData] to be sorted or filtered.
     * @return A new list of [DoctorsData] modified based on the specified type.
     */
    private fun changeFilter(type: FilterType, data: List<DoctorsData>): List<DoctorsData> {
        return when (type) {
            FilterType.A_TO_Z -> orderAtoZData(data)
            FilterType.Z_TO_A -> orderZtoAData(data)
            FilterType.MALE -> maleData(data)
            FilterType.FEMALE -> femaleData(data)
            FilterType.FAVORITE -> favoriteData(data)
        }
    }

    /**
     * Sorts a list of doctors in alphabetical order by name.
     *
     * This function takes a list of [DoctorsData] and sorts it in ascending order based on the `name` property.
     *
     * @param data The list of [DoctorsData] to be sorted.
     * @return A new list of [DoctorsData] sorted alphabetically by name.
     */
    private fun orderAtoZData(data: List<DoctorsData>): List<DoctorsData> {
        val orderedData = data.sortedBy { it.name }
        return orderedData
    }

    /**
     * Sorts a list of doctors in reverse alphabetical order by name.
     *
     * This function takes a list of [DoctorsData] and sorts it in descending order based on the `name` property.
     *
     * @param data The list of [DoctorsData] to be sorted.
     * @return A new list of [DoctorsData] sorted in reverse alphabetical order by name.
     */
    private fun orderZtoAData(data: List<DoctorsData>): List<DoctorsData> {
        val orderedData = data.sortedByDescending { it.name }
        return orderedData
    }

    /**
     * Filters a list of doctors to include only male doctors.
     *
     * This function takes a list of [DoctorsData] and returns a new list containing only the doctors
     * whose `type` property is equal to `"male"`.
     *
     * @param data The list of [DoctorsData] to be filtered.
     * @return A new list of [DoctorsData] containing only male doctors.
     */
    private fun maleData(data: List<DoctorsData>): List<DoctorsData> {
        val orderedData = data.filter { it.type == "male" }
        return orderedData
    }

    /**
     * Filters a list of doctors to include only female doctors.
     *
     * This function takes a list of [DoctorsData] and returns a new list containing only the doctors
     * whose `type` property is equal to `"female"`.
     *
     * @param data The list of [DoctorsData] to be filtered.
     * @return A new list of [DoctorsData] containing only female doctors.
     */
    private fun femaleData(data: List<DoctorsData>): List<DoctorsData> {
        val orderedData = data.filter { it.type == "female" }
        return orderedData
    }

    /**
     * Filters a list of doctors to include only those marked as favorites.
     *
     * This function takes a list of [DoctorsData] and returns a new list containing only the doctors
     * whose `favorite` property is set to `true`.
     *
     * @param data The list of [DoctorsData] to be filtered.
     * @return A new list of [DoctorsData] containing only favorite doctors.
     */
    private fun favoriteData(data: List<DoctorsData>): List<DoctorsData> {
        val orderedData = data.filter { it.favorite }
        return orderedData
    }
}