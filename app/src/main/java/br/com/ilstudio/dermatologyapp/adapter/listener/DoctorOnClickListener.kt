package br.com.ilstudio.dermatologyapp.adapter.listener

import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData

class DoctorOnClickListener(val clickListener: (doctor: DoctorsData) -> Unit) {
    fun onClick(contact: DoctorsData) = clickListener
}

class DoctorOnClickFavListener (val clickListener: (doctor: DoctorsData) -> Unit) {
    fun onClick(contact: DoctorsData) = clickListener
}
