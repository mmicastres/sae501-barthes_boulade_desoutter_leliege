package com.example.hiker

import androidx.compose.runtime.Composable


class Person constructor(_Name: String, _Acquired:Boolean=false ) {
    val Name = _Name
    var Acquired = _Acquired

    fun characters(){
        var person1=Person("Merlin l’Enjambeur",false)
        var person2=Person("Alexandre",true)
        var person3=Person("Louna",false)
        var person4=Person("Marche Simpson",false)
        var person5=Person("Louis",false)
        var person6=Person("Nino",true)
    }


}
