import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.system.measureTimeMillis

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main() {
    //TODO 1.	Создайте класс Storage – это хранилище, в котором находится текстовая
    // переменная text, в ней хранится басня Крылова «Мартышка и очки». В классе с
    // функцией main необходимо написать функцию, которая преобразует строку в список
    // строк. Написать функцию getList(text: String), которая отправляет данные этого
    // списка в канал с задержкой 10L.
    // В функции main необходимо получить эти данные и вернуть их в виде исходного
    // текста, сохранить результат в переменную stringResult. Посчитать время,
    // затраченное на получение данных в main. Вывести в консоль полученный результат
    // в stringResult.
    println("1. Задание\n")
    val decoration = Decoration()

    val storage = Storage()


    val storageList = storageList(storage.textSet)

    var getStringList = mutableListOf<String>()
    //переменная для получения выходных данных канала

    var stringResult = ""
    //результат задания - строка

    var time = measureTimeMillis {
        coroutineScope {
            val channel = getList(storageList)
            channel.consumeEach {
                getStringList.add(it)
            }
        }
        stringResult = getStringList.toList().joinToString(
            separator = "\n"
        )
        println(stringResult)
    }

    decoration.getLine("_")
    println("Затраченное время $time мс")

    decoration.getLine("=")

    //TODO 2.	Использовать тестовую переменную первой задачи. Кроме функции
    // getList(text: String) и getStringList(text: String), необходимо написать функцию
    // modifiedList, объединяющую каналы и отправляет данные в новый канал, которые
    // представляют следующий вид, у каждого элемента списка берется первый элемент и
    // переводится в верхний регистр. В итоге результат объединения каналов со всеми
    // преобразованиями в функции main возвращается в строку stringResult.
    // Кроме того, необходимо полученную строку разбить и поместить в список <String>.
    // Вывести в консоль полученный список, состоящий из первых символов каждого слова,
    // затраченное время на вышеуказанные операции.
    println("2. Задание\n")

    val storageStringList = storageListString(storage.textSet)
    getStringList.clear()
    val charListOut: List<Char>

    time = measureTimeMillis {
        coroutineScope {
            val channelOne = getList(storageList)
            val channelTwo = getStringList(storageStringList)
            val channelThree = modifiedList(channelOne, channelTwo)
            channelThree.consumeEach {
                getStringList.add(it)
            }
        }
        stringResult = getStringList.toList().joinToString(
            separator = "\n"
        )

        charListOut = firstCharString(stringResult)
        println(charListOut)

    }

    decoration.getLine("_")
    println("Затраченное время $time мс")
    println("\nВыходная строка из канала, созданного функцией modifiedList [для справки]:\n$stringResult")

    decoration.getLine("^")
}


fun storageList(stroke: String): List<String> {
    //функция, которая преобразует строку в список строк
    val list = stroke.split("\n").toList()
    return list
}

fun storageListString(stroke: String): List<String> {
    //функция, которая преобразует строку в список слов
    val list = stroke.replace("[;,.—«»!-:\n ]".toRegex(), "|")
    val listOut = list.split("|").toMutableList()
    listOut.removeAll(listOf(""))
    return listOut
}

suspend fun CoroutineScope.getList(list: List<String>): ReceiveChannel<String> = produce {
    //функция, которая отправляет данные списка в канал с задержкой.
    for (i in list) {
        delay(10L)
        send(i)
    }
    channel.close()
}


suspend fun CoroutineScope.getStringList(list: List<String>): ReceiveChannel<String> = produce {
    //функция, которая отправляет данные списка в канал с задержкой.
    for (i in list) {
        delay(10L)
        send(i)
    }
}

suspend fun CoroutineScope.modifiedList(
    //функция, которая отправляет первый элемент двух каналов в верхнем регистре
    channelOne: ReceiveChannel<String>,
    channelTwo: ReceiveChannel<String>
): ReceiveChannel<String> = produce {
    var k = 1

    channelOne.consumeEach {
        while (k <= 1) {
            send(it.uppercase())
            k++
        }
    }
    k--
    channelTwo.consumeEach {
        while (k <= 1) {
            send(it.uppercase())
            k++
        }
    }

}

fun firstCharString(text: String): List<Char> {
    //функция, которая создаёт список из первых символов слов строки
    val list = storageListString(text)
    val listOut = list.map { it.first() }
    return listOut
}