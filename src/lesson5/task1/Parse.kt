@file:Suppress("UNUSED_PARAMETER")

package lesson5.task1

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
    val months = listOf("", "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря")
    val numbers = '0'..'9'
    val parts = str.split(" ")
    if ((parts.size != 3) || (parts[2].length != 4)) return ""
    for (i in 0..3) {
        if (parts[2][i] !in numbers) return ""
    }
    var days = parts[0]
    if ((parts[0].length == 1) && (parts[0][0] in numbers)) days = twoDigitStr(parts[0].toInt())
    if ((days[0] in numbers) && (days[1] in numbers)
            && (parts[1] in months))
        return String.format("%s.%s.%s", twoDigitStr(parts[0].toInt()),
                twoDigitStr(months.indexOf(parts[1])), parts[2]) else return ""

}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateDigitToStr(digital: String): String {
    val months = listOf("", "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря")
    val numbers = '0'..'9'
    val parts = digital.split(".")
    if ((parts.size != 3) || (parts[0].length != 2) || (parts[1].length != 2)) return ""
    for (part in parts) {
        for (symbol in part)
            if (symbol !in numbers) return ""
    }
    if (!(0 < parts[1].toInt()) || !(parts[1].toInt() < 13)) return ""
    val days = if (parts[0][0].toString() == "0") parts[0][1].toString() else parts[0]
    val mon = months[parts[1].toInt()]
    return String.format("%s %s %s", days, mon, parts[2])
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
    if (phone.length == 0) return ""
    val numbers = '0'..'9'
    when {
        phone.first() in numbers -> {
            var result = ""
            for (symbol in phone)
                when {
                    (symbol in numbers) -> result += symbol.toString()
                    (symbol == ')') || (symbol == '(') || (symbol == '-') || (symbol == '+') || (symbol == ' ') -> null
                    else -> return ""
                }
            return result
        }

        phone.first() == '+' -> {
            var result = "+"
            for (symbol in phone)
                when {
                    (symbol in numbers) -> result += symbol.toString()
                    (symbol == ')') || (symbol == '(') || (symbol == '-') || (symbol == '+') || (symbol == ' ') -> null
                    else -> return ""
                }
            return result
        }
    }
    return ""
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
    if (jumps.length == 0) return -1
    val numbers = '0'..'9'
    val allowedSymbols = listOf("%", "-")
    val parts = jumps.split(" ")
    var buf = ""
    var bestScore = -1
    for (part in parts) {
        when {
            (part.length == 1) && (part !in allowedSymbols) -> return -1

            (part.length == 1) && (part in allowedSymbols) -> null
            else -> {
                for (symbol in part)
                    if (symbol !in numbers)
                        buf += symbol.toString() else return -1
                if (buf.toInt() > bestScore) bestScore = buf.toInt()
                buf = ""
            }
        }
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
    if (jumps.length == 0) return -1
    val numbers = '0'..'9'
    val allowedSymbols = listOf('%', '-', '+')
    val parts = jumps.split(" ")
    var buf = ""
    var bestScore = -1
    var type = 1
    var counted = false
    //1 - число, -1 - %%-
    for (part in parts) {
        when {
            type == 1 -> {
                buf = ""
                for (symbol in part)
                    if (symbol in numbers) buf += symbol.toString() else return -1
            }
            type == -1 -> {
                for (symbol in part)
                    if (symbol !in allowedSymbols) return -1 else
                        when {
                            symbol == '-' -> counted = false
                            symbol == '+' -> counted = true
                        }
            }
        }
        if (counted && (buf.toInt() > bestScore) && (type == -1)) bestScore = buf.toInt()
        type *= (-1)
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
    if (expression.length == 0) return -1
    val numbers = '0'..'9'
    val allowedSymbols = listOf("%", "-", "+")
    val parts = expression.split(" ")
    var buf = ""
    var result = 0
    var type = 1
    var plus = true
    for (part in parts) {
        when {
            type == 1 -> {
                for (symbol in part) {
                    buf = ""
                    if (symbol in numbers) buf += symbol.toString() else
                        IllegalArgumentException("Выражение введено некорректно. " +
                                "Введите его правильно, как в этом примере: \"2 + 31 - 40 + 13\"")
                }
            }
            type == -1 -> {
                if ((part.length > 1) || (part !in allowedSymbols))
                    IllegalArgumentException("Выражение введено некорректно. " +
                            "Введите его правильно, как в этом примере: \"2 + 31 - 40 + 13\"") else
                    when {
                        part == "+" -> plus = true
                        part == "-" -> plus = false
                    }
            }
        }
        if (type == 1)
            when {
                plus -> {
                    result += buf.toInt()
                    buf = ""
                }
                else -> {
                    result -= buf.toInt()
                    buf = ""
                }
            }
        type *= -1
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
    if (str.length == 0) return -1
    val parts = str.toLowerCase().split(" ")
    var i = 1
    val partsCount = parts.count() - 1
    var symbolsCount = -1
    for (n in 0 until parts.count()){
        for(j in i..partsCount){
            if(parts[n] == parts[j]) {
                symbolsCount = 0
                for (k in 0..(i - 1)) symbolsCount += parts[k].length /////////////ask for the help/////////////
                symbolsCount -= parts[i - 1].length
                symbolsCount += i - 1
            }
        }
        ++i
    }
    return symbolsCount
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
    if (description.length == 0) return ""
    val numbers = '0'..'9'
    val parts = description.split(" ")
    var productBuf = ""
    var resultProduct = ""
    var priceBuf = "0.0"
    var bestPrice = 0.0
    var type = 1
    //1 - число, -1 - %%-
    for (part in parts) {
        when {
            type == 1 -> productBuf = part

            type == -1 -> {
                priceBuf = ""
                if(!((part[part.length - 1] == ';') ||
                        ((part == parts[parts.count() - 1]) && (productBuf == parts[parts.count() - 2])))) return ""
                if (part[part.length - 1] == '.') return ""
                var dotCheck = false

                for (symbol in 0..(part.length - 2))
                    when{
                        part[symbol] in numbers -> priceBuf += part[symbol].toString()
                        part[symbol] == '.' -> {
                            if (!dotCheck) dotCheck = true else return ""
                            priceBuf += "."
                        }
                        else -> return ""
                    }
            }
        }

        if ((bestPrice < priceBuf.toDouble()) && (type == -1)) {
            bestPrice = priceBuf.toDouble()
            resultProduct = productBuf
        }
        type *= (-1)
    }
    return resultProduct
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int = TODO()

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
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> = TODO()
