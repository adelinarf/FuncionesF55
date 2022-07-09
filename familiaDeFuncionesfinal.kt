import kotlin.system.*

// 15-10484
// X = 4
// Y = 8
// Z = 4
//alpha = 5
//beta = 5
/* Este programa implementa la definicion de la familia de funciones para F 5,5 y se crean varias funciones para este calculo.
 * Una funcion recursiva, una funcion de cola no optimizada,una funcion de cola optimizada y una funcion iterativa basada en 
 * la funcion recursiva de cola optimizada.
 * */

/* La funcion F55 es la funcion recursiva que realiza el calculo de F55, es una traduccion de la definicion de la funcion.
 * */
fun F55(n : Int, alpha : Int, beta: Int) : Int{
	if (0<=n && n<(alpha*beta)){
		return n
	}
	if (n>=(alpha*beta)){
		var suma = 0
		for (i in 1 .. alpha) {
			suma += F55(n-beta*i,alpha,beta)
		}
		return suma	 
	}
    return 0
}

/* La funcion F55DeCola es la funcion recursiva de cola no optimizada, ya que se basa en una funcion de suma
 * y aun asi se llama de nuevo a esta funcion durante la suma, por esto no es tan rapida como las siguientes funciones, pero
 * si mas rapida que la funcion recursiva.
 * */
fun F55DeCola(n : Int ,alpha : Int ,beta : Int) : Int {
	if (n>=0 && n<(alpha*beta)){
		return n
	}
	if (n>=(alpha*beta)){
		return sumarF55(n,alpha,0,alpha,beta)
	}
	return 0
}
/*La funcion sumarF55 se encarga de sumar los valores de la funcion F55DeCola.
 * */
fun sumarF55(n : Int ,i : Int,Sum : Int,alpha : Int,beta : Int) : Int {
	if (i == 0){
		return Sum
	}
	return sumarF55(n, i-1,Sum+F55DeCola(n-(beta*i),alpha,beta),alpha,beta)
}

/*
 * La funcion F55DeCola2 es la funcion recursiva de cola optimizada, es muy rapida gracias a que utiliza un diccionario
 * o map mutable de Kotlin para alojar los valroes que se van calculando y por ello solo requiere de una funcion muy sencilla
 * con calculos O(1) que se llama a si misma la misma cantidad de veces, del numero que se introduzca en la funcion, por lo que
 * en algun momento veremos el error Stack Overflow.
 */
fun F55DeCola2(n : Int, alpha : Int, beta : Int) : Int{
	var mem : MutableMap<Int,Int>  = mutableMapOf()
	for (i in 0 `until`alpha*beta){
		mem[i] = i
	}
	if (0<=n && n<(alpha*beta)){
		return mem[n]!!
	}
	fun cola_(i: Int): Int{
		mem[i] = (1..alpha).map { mem[i-beta*it]!! }.sum()
		if (n==i){
			return mem[n]!!
		}
		return cola_(i+1)
	}
	return cola_(alpha*beta)
}
/*La funcion F55Iterativa es la version iterativa de la funcion F5,5 y se ha basado en la version modificada de la recursion de cola
 * anterior, debido a que no depende del stack puede llamar a numeros mas grandes que la version de cola e igualmente usa un diccionario
 * o map mutable para alojar los valores calculados.
 * */
fun F55Iterativa(n: Int, alpha: Int, beta: Int):Int{
	var mem : MutableMap<Int,Int>  = mutableMapOf()
	for (i in 0 `until`alpha*beta){
		mem[i] = i
	}
	if (0<=n && n<(alpha*beta)){
		return mem[n]!!
	}
  var s = 0
	for (i in alpha*beta .. n){
		mem[i] = (1..alpha).map { mem[i-beta*it]!! }.sum()
		if (n==i){
			s = mem[n]!!
		}
	}
  return s
}

/*
 *La funcion test es una funcion de alto orden, que recibe otra funcion y un valor y aplica la funcion pasada a un conjunto de valores. 
 */
fun test(n:Int,func : (Int,Int,Int) -> Int) : Int {
  var X = 4
  var Y = 8
  var Z = 4
  var alpha = ((X+Y)%5)+3
  var beta  = ((Y+Z)%5)+3
  return func(n,alpha,beta)
}
/*La funcion tests realiza las pruebas individuales de cada una de las funciones que hemos mencionado anteriormente y calcula el tiempo
 * de su ejecucion. Es tambien una funcion de alto orden.
 * */
fun tests(func : (Int,Int,Int) -> Int) : MutableList<Long>{
  var lista = listOf(1,3,5,10,26,14,19,20,40,50,80,100,150,180)
  var resultados : MutableList<Long> = mutableListOf()
  for (x in 0..lista.size-1){
    var timeInMillis = measureTimeMillis {
        test(lista[x],func)
    }
    resultados.add(timeInMillis)
    println("Prueba ${x} para ${lista[x]} tiene un tiempo de: ${timeInMillis} milisegundos")
  }
  return resultados
}
/*La funcion pruebas ejecuta todas las pruebas que fueron posibles para TODAS las funciones. Aunque hay ciertas funciones que pueden
 * realizar calculos mucho mas alla de los valores aqui colocados, no se incluyeron debido a que es imposible para otras funciones correrlos
 * o les tomaria un tiempo muy largo.
 * */
fun pruebas(){
  println("Prueba para funcion recursiva\n")
  var t1 = tests(::F55)
  println("El tiempo medio de ejecucion es: ${t1.sum()/t1.size}\n")
  println("Prueba para funcion recursiva de cola no optimizada\n")
  var t2 = tests(::F55DeCola)
  println("El tiempo medio de ejecucion es: ${t2.sum()/t2.size}\n")
  println("Prueba para funcion recursiva de cola optimizada\n")
  var t3 = tests(::F55DeCola2)
  println("El tiempo medio de ejecucion es: ${t3.sum()/t3.size}\n")
  println("Prueba para funcion de iterativa\n")
  var t4 = tests(::F55Iterativa)
  println("El tiempo medio de ejecucion es: ${t4.sum()/t4.size}\n")
}

/*La funcion main se ha implementado a modo de REPL, se pueden introducir las palabras RECURSIVO, COLA e ITERATIVO seguidas de un numero
 * para realizar el calculo de la funcion deseada con dicho numero. Se sale del programa al escribir SALIR. Tambien se pueden realizar 
 * las mismas pruebas con la palabra PRUEBAS
 * */
fun main(){
  var X = 4
  var Y = 8
  var Z = 4
  var alpha = ((X+Y)%5)+3
  var beta  = ((Y+Z)%5)+3
  println("Funciones F 5,5")
  var esperando = true
  while (esperando){
    val entrada = readLine()
    if (entrada?.indexOf("SALIR") == 0){
      esperando = false
      println("Has salido del programa")
    }
    else if (entrada?.indexOf("RECURSIVO") == 0){
      var insertados = entrada.split(" ")
      if (insertados[1].toInt() >= 0){
        println(F55(insertados[1].toInt(), alpha,beta))
      }
      else{
        println("Los numeros deben ser mayores o iguales a 0")
      }
    }
    else if (entrada?.indexOf("COLA") == 0){
      var insertados = entrada.split(" ")
      if (insertados[1].toInt() >= 0){
        println("Funcion de recursion de cola no optimizada:")
        println(F55DeCola(insertados[1].toInt(), alpha,beta))
        println("Funcion de recursion de cola optimizada:")
        println(F55DeCola2(insertados[1].toInt(), alpha,beta))
      }
      else{
        println("Los numeros deben ser mayores o iguales a 0")
      }
    }
    else if (entrada?.indexOf("ITERATIVO") == 0){
      var insertados = entrada.split(" ")
      if (insertados[1].toInt() >= 0){
        println(F55Iterativa(insertados[1].toInt(), alpha,beta))
      }
      else{
        println("Los numeros deben ser mayores o iguales a 0")
      }
    }
    else if (entrada?.indexOf("PRUEBAS") == 0){
      pruebas()
    }
    else{
      println("La entrada no es correcta")
    }
  }
}
