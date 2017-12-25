@file:Suppress("UNUSED_PARAMETER")

package lesson5.task1
import java.lang.Math.*
/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main(args: Array<String>) {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateStrToDigit(str: String): String {
    val months = listOf("января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря")
    val parts = str.split(" ")
    try {
        if ((parts.count() != 3) || (parts[0].toInt() !in 1..31) || (parts[2].toInt() < 0)
                || (parts[1] !in months)) return ""
    }
    catch(e: NumberFormatException) {return ""}

    return String.format("%02d.%02d.%d", parts[0].toInt(),
                months.indexOf(parts[1]) + 1, parts[2].toInt())

}

/**
* Средняя
*
* Дата представлена строкой вида "15.07.2016".
* Перевести её в строковый формат вида "15 июля 2016".
* При неверном формате входной строки вернуть пустую строку
*/
fun dateDigitToStr(digital: String): String {
    val months = listOf("января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря")
    val parts = digital.split(".")
    try {
        if (parts.count() != 3 || (parts[0].toInt() !in 1..31)
                || (parts[1].toInt() !in 1..12) || (parts[2].toInt() < 0)) return ""
    }
    catch(e: NumberFormatException) {return ""}
    return String.format("%d %s %s", parts[0].toInt(), months[parts[1].toInt() - 1], parts[2])
}

/**
* Средняя
*
* Номер телефона задан строкой вида "+7 (921) 123-45-67".
* Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
* Может присутствовать неограниченное количество пробелов и чёрточек,
* например, номер 12 --  34- 5 -- 67 -98 тоже следует считать легальным.
* Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
* "+79211234567" или "123456789" для приведённых примеров.
* Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
* При неверном формате вернуть пустую строку
*/
fun flattenPhoneNumber(phone: String): String {
    val result = Regex("""[-\s]""").replace(phone, "")
    if(!result.matches(Regex("""^(\+[0-9]+)?(\([0-9]+\))?([0-9]+)$"""))) return ""
    return Regex("""[()]""").replace(result,"")
}

/**
* Средняя
*
* Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
* "706 - % 717 % 703".
* В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
* число соответствует удачному прыжку, - пропущенной попытке, % заступу.
* Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
* При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
*/
fun bestLongJump(jumps: String): Int {
    if (jumps.isEmpty()) return -1
    val allowedSymbols = setOf('%', '-')
    val parts = jumps.split(" ")
    var buf = 0
    var bestScore = -1
    var isItNumber = false
    for (part in parts) {
        for (symbol in part) {
            when {
                symbol in '0'..'9' -> isItNumber = true
                symbol in allowedSymbols -> if (isItNumber) return -1
                else -> return -1
            }
        }
        if(isItNumber) buf = part.toInt()
        if (isItNumber && (buf > bestScore)) bestScore = buf
        isItNumber = false
    }
    return bestScore
}

/**
* Сложная
*
* Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
* "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
* Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
* Высота и соответствующие ей попытки разделяются пробелом.
* Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
* При нарушении формата входной строки вернуть -1.
*/
fun bestHighJump(jumps: String): Int {
    if (jumps.isEmpty()) return -1
    val allowedSymbols = setOf('%', '-', '+')
    val parts = jumps.split(" ")
    var buf = 0
    var bestScore = -1
    var isNumber = true
    //true - число, false - %%-
    for (part in parts) {
        when {
            isNumber -> {
                buf = 0
                try{
                    buf += part.toInt()
                }
                catch(e: NumberFormatException){return -1}
            }
            !isNumber ->
                for (symbol in part)
                    if((symbol == '+')&& (buf > bestScore) && !isNumber) bestScore = buf
                    else
                    if (symbol !in allowedSymbols) return -1
        }
        isNumber = !isNumber
    }
    return bestScore
}

/**
* Сложная
*
* В строке представлено выражение вида "2 + 31 - 40 + 13",
* использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
* Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
* Вернуть значение выражения (6 для примера).
* Про нарушении формата входной строки бросить исключение IllegalArgumentException
*/
fun plusMinus(expression: String): Int {
    if (expression.isEmpty()) throw IllegalArgumentException()
    val format = Regex("""^([0-9]+)(\s+(\+|-)\s+([0-9]+))*$""")
    if(!(expression.matches(format))) throw IllegalArgumentException()
    val parts = expression.split(" ")
    var buf = 0
    var result = 0
    var isNumber = true
    var plus = true
    for (part in parts) {
        when {
            isNumber -> {
                buf += part.toInt()
                when {
                    plus -> {
                        result += buf.toString().toInt()
                        buf = 0
                    }
                    else -> {
                        result -= buf.toString().toInt()
                        buf = 0
                    }
                }
            }
            !isNumber -> {
                when {
                    part == "+" -> plus = true
                    part == "-" -> plus = false
                }
            }
        }
        isNumber = !isNumber
    }
    return result
}

/**
* Сложная
*
* Строка состоит из набора слов, отделённых друг от друга одним пробелом.
* Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
* Слова, отличающиеся только регистром, считать совпадающими.
* Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
* Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
*/
fun firstDuplicateIndex(str: String): Int {
    if (str.isEmpty()) return -1
    val parts = str.toLowerCase().split(" ")
    var result = 0
    var i = 0
    var wordFound = false
    while(i < parts.count() - 1){
        if (parts[i].toLowerCase() == parts[i + 1].toLowerCase()) {
            wordFound = true
            break
        }
        ++i
    }
    if (wordFound) {
        for (j in 0 until i) result += parts[j].length + 1 //учитывая пробел
        return result
    }
    else return -1
}

/**
* Сложная
*
* Строка содержит названия товаров и цены на них в формате вида
* "Хлеб 39.9; Молоко 62.5; Курица 184.0; Конфеты 89.9".
* То есть, название товара отделено от цены пробелом,
* а цена отделена от названия следующего товара точкой с запятой и пробелом.
* Вернуть название самого дорогого товара в списке (в примере это Курица),
* или пустую строку при нарушении формата строки.
* Все цены должны быть положительными
*/
fun mostExpensive(description: String): String {
    if (description.isEmpty()) return ""
    val parts = description.split(" ")
    val productBuf = StringBuilder("")
    val resultProduct = StringBuilder("")
    var priceBuf = 0.0
    var bestPrice = 0.0
    var isNumber = false
    //true - число, false - продукт
    for (part in parts) {
        when {
            !isNumber -> {
                    productBuf.delete(0, productBuf.length)
                    productBuf.append(part)
            }
            isNumber -> {
                if ((part[part.length - 1] != ';') && (part != parts[parts.count() - 1])
                        || (part[part.length - 1] == '.') || (part[0] == '.')) return ""
                try {
                    priceBuf = Regex(""";""").replace(part, "").toDouble()
                }
                catch (e: NumberFormatException){return ""}
            }
        }
        if ((bestPrice <= priceBuf) && (isNumber)) {
            bestPrice = priceBuf
            resultProduct.delete(0, resultProduct.length)
            resultProduct.append(productBuf)
        }
        isNumber = !isNumber
    }
    return resultProduct.toString()
}

/**
* Сложная
*
* Перевести число roman, заданное в римской системе счисления,
* в десятичную систему и вернуть как результат.
* Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
* 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
* Например: XXIII = 23, XLIV = 44, C = 100, LXIV = -1
*
* Вернуть -1, если roman не является корректным римским числом
*/
fun fromRoman(roman: String): Int {
    var result = 0
    if (roman.isEmpty()) return -1
    val romanNumbers = listOf("M","CM","D","CD","C","XC","L","XL",
            "X","IX","VIII","VII","VI","V","IV","III","II", "I")
    val arabNumbers = listOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
    //listOf(1000 to "M" , 900 to "CM", 500 to "D", 400 to "CD", 100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
    // 10 to "X", 9 to "IX", 8 to "VIII", 7 to "VII", 6 to "VI", 5 to "V", 4 to "IV", 3, 2, 1)
    val romanToArab = hashMapOf("M" to 1000, "CM" to 900, "D" to 500, "CD" to 400, "C" to 100, "XC" to 90, "L" to 50, "XL" to 40, "X" to 10,
            "IX" to 9, "VIII" to 8, "VII" to 7, "VI" to 6, "V" to 5, "IV" to 4, "III" to 3, "II" to 2, "I" to 1)
    val buf = StringBuilder("")
    var bufPreviousInNumber = 0
    for(symbol in roman){
        buf.append(symbol)
        if (symbol.toString() !in romanNumbers) return -1
        if(buf.length == 2){
            if (buf.toString() in romanNumbers){
                val currentBufInNumber = romanToArab[buf.toString()] !!
                if((bufPreviousInNumber < currentBufInNumber) && bufPreviousInNumber != 0) return -1
                result += currentBufInNumber
                bufPreviousInNumber = currentBufInNumber
                buf.delete(0,2)
            }
            else {
                //buf00 - левый литерал из двух в buf
                val buf00 = romanToArab[buf[0].toString()] !!
                if((bufPreviousInNumber < buf00) && bufPreviousInNumber != 0) return -1
                result += buf00
                bufPreviousInNumber = buf00
                buf.delete(0,1)
            }
        }
    }
    try {
        if (buf.length == 1) {
            val currentBufInNumber = romanToArab[buf.toString()]!!

            if ((buf.toString() in romanNumbers) &&
                    !((bufPreviousInNumber < currentBufInNumber) && bufPreviousInNumber != 0))
                return result + currentBufInNumber
            else
                return -1
        }

    }
    catch(e: ArrayIndexOutOfBoundsException) {return -1}
    if ((result in arabNumbers) && (roman != romanNumbers[arabNumbers.indexOf(result)])) return -1
    return result
}

/**
* Очень сложная
*
* Имеется специальное устройство, представляющее собой
* конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
* Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
* Каждая команда кодируется одним специальным символом:
*	> - сдвиг датчика вправо на 1 ячейку;
*  < - сдвиг датчика влево на 1 ячейку;
*	+ - увеличение значения в ячейке под датчиком на 1 ед.;
*	- - уменьшение значения в ячейке под датчиком на 1 ед.;
*	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
*  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
*	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
*  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
*      (комбинация [] имитирует цикл)
*  пробел - пустая команда
*
* Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
*
* После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
* Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
* значении под датчиком равном 0) и пробелы.
*
* Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
* Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
*
* Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
* То же исключение формируется, если у символов [ ] не оказывается пары.
* Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
* Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
* то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
* IllegalArgumentException.
* IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
*
*/
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    var positionId = cells / 2
    val allowedCommandsAndSymbols = setOf('[', ']', '+', '-', '>', '<', ' ')
    val cellRow = MutableList(cells, { 0 })
    if (commands.isEmpty()) return cellRow
    var cmd_counter = limit // command count
    var k = 0 // command id
    var braceBalance = 0
    for (command in commands) {
        when {
            command == '[' -> ++braceBalance
            command == ']' -> --braceBalance
            command !in allowedCommandsAndSymbols -> throw IllegalArgumentException()
        }
        if (braceBalance < 0) throw IllegalArgumentException()
    }
    if (braceBalance != 0) throw IllegalArgumentException()
    fun bodyCycleSizeFun(k: Int): Int {         // узнать размер текущего цикла
        var z = 1
        while (commands[k + z] != ']') {
            if (commands[k + z] == '[') z += bodyCycleSizeFun(k + z)
            ++z
        }
        return z
    }

    fun cycle(): MutableList<Int> {
        var cycleDeactivated = false
        var cycleBodySize = 0
        while (!(cycleDeactivated) && (cmd_counter > 0)) {
            --cmd_counter
            when (commands[k]) {
                '>' -> if (positionId + 1 >= cells) throw IllegalStateException() else positionId += 1
                '<' -> if (positionId - 1 < 0) throw IllegalStateException() else positionId -= 1
                '+' -> cellRow[positionId] += 1
                '-' -> cellRow[positionId] -= 1
                '[' -> {
                    cycleBodySize += bodyCycleSizeFun(k) //размер текущего цикла + цикл,
                    // который берёт начало в этом цикле
                    if (cellRow[positionId] != 0) {
                        ++k; cycle() //тогда войти в цикл
                    }
                    else { //тогда пропустить этот цикл
                        var j = 0
                        val size = bodyCycleSizeFun(k)
                        while (j < size) {
                            ++k
                            ++j
                        }
                    }
                }
                ']' -> {
                    if (cellRow[positionId] != 0) {
                        while (cycleBodySize > -1) {
                            --k
                            --cycleBodySize
                        }

                    } else {
                        cycleDeactivated = true
                        --k
                    }
                }
                ' ' -> null
                else -> throw IllegalArgumentException()
            }
            ++cycleBodySize //размер текущего цикла
            ++k
            if (k >= commands.length) return cellRow //если дошёл до конца строки команд
        }
        return cellRow
    }
    return cycle()
}