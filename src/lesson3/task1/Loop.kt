@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import lesson1.task1.numberRevert
import java.lang.Math.*

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result = result * i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    for (m in 2..Math.sqrt(n.toDouble()).toInt()) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
        when {
            n == m -> 1
            n < 10 -> 0
            else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
        }

/**
 * Тривиальная
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 */
fun digitNumber(n: Int): Int = when {
    n < 10 -> 1
    else -> digitNumber(n / 10) + 1
}

/**
 * Простая
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int {
    var f1 = 1
    var f2 = 1
    var f3 = 1
    var k = 2
    if ((n == 1)||(n == 2)) return 1
    while(k < n){
        f3 = f1 + f2
        f1 = f2
        f2 = f3
        ++k
    }
    return f3
}


/**
 * Простая
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */
fun lcm(m: Int, n: Int): Int {
    var p = 0
    for (i in 1..n * m) {
        if ((i % n == 0) && (i % m == 0)) return i
    }
    return p
}

/**
 * Простая
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    var minD = 2
    while (true) {
        if (n % minD == 0) return minD
        ++minD
    }
    return minD
}

/**
 * Простая
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int {
    var maxD = n / 2
    while (true) {
        if (n % maxD == 0) return maxD
        --maxD
    }
    return maxD
}

/**
 * Простая
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean {
    var k = 2
    while (k <= min(m, n)) {
        if ((m % k == 0) && (n % k == 0)) return false
        ++k
    }
    return true
}

/**
 * Простая
 *
 * Для заданных чисел m и n, m <= n, определить, имеется ли хотя бы один точный квадрат между m и n,
 * то есть, существует ли такое целое k, что m <= k*k <= n.
 * Например, для интервала 21..28 21 <= 5*5 <= 28, а для интервала 51..61 квадрата не существует.
 */

fun squareBetweenExists(m: Int, n: Int): Boolean {
    var k = 0.0
    while (k * k <= n) {
        if ((m <= pow(k, 2.0)) && (pow(k, 2.0) <= n)) return true
        ++k
    }
    return false
}

/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 */
fun sin(x: Double, eps: Double): Double {
    var normal = x
    if (x >= 2 * PI) normal = x % (2 * PI)
    var k = normal
    var l = normal
    var i = 3.0
    while (abs(k) >= eps) {
        k = (-k * normal * normal) / (i * (i + 1.0))
        l += k
        i += 2.0
    }
    //if (ceil(l) <= -2.0) return ceil(l) + 2.0 else if (ceil(l) >= 2.0) return ceil(l) - 2.0 else
    return  l
}

/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 */
fun cos(x: Double, eps: Double): Double {
    var normal = x
    if (x >= 2 * PI) normal = x % (2 * PI)
    var k = 1.0
    var l = 1.0
    var i = 1.0
    while (abs(k) > eps) {
        k = (-k * normal * normal) / (i * (i + 1.0))
        l += k
        i += 2.0
    }
    return l
}

/**
 * Средняя
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 * Не использовать строки при решении задачи.
 */
fun revert(n: Int): Int {
    var k = n
    var i = 0
    var count = 0
    while (k > 0) {
        k = k / 10
        ++i
        ++count
    }

    count = (pow(10.0, count.toDouble() - 1)).toInt()

    k = 0
    var s = n
    s = n
    while (i > 0) {
        k = k + (s % 10) * count
        s = s / 10
        count = count / 10
        --i
    }
    return k
}

/**
 * Средняя
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 */
fun isPalindrome(n: Int): Boolean {
    var k = n
    var count = 0
    while (k > 0) {
        k = k / 10
        ++count
    }
    var t = pow(10.0, (count - 1).toDouble()).toInt()
    k = 10
    var s = n
    for (i in 1..(count / 2 + count % 2)) {
        if (s / t != (n % k) * 10 / k) return false
        k = k * 10
        s = s % t
        t /= 10
    }
    return true
}

/**
 * Средняя
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 */
fun hasDifferentDigits(n: Int): Boolean {
    if (n < 10) return false
    var k = 0
    var count = 1
    val prev = n % 10
    while (n / count != 0) {
        k = (n / count) % 10
        if (k != prev) return true
        count *= 10
    }
    return false
}

/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 */
fun squareSequenceDigit(n: Int): Int {
    var total = 0
    var k = 1
    var count = 10
    var numberOfDec = 1
    var cloneTotal = 1
    var i = 1
    while (true) {
        total = k * k
        cloneTotal = total
        count = 10
        i = 1
        while (total / count != 0) {
            count *= 10
            ++i
        }
        count /= 10
        if (total >= 10) {
            while (i != 0) {
                if (numberOfDec == n) return cloneTotal / count
                ++numberOfDec
                cloneTotal %= count
                count /= 10
                --i
            }
            ++k
        } else {
            if (numberOfDec == n) return cloneTotal
            ++k
            ++numberOfDec
        }
    }
}

/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 */
fun fibSequenceDigit(n: Int): Int {
    var total = 0
    var k = 1
    var count = 10
    var numberOfDec = 1
    var cloneTotal = 1
    var i = 1
    while (true) {
        total = fib(k)
        cloneTotal = total
        count = 10
        i = 1
        while (total / count != 0) {
            count *= 10
            ++i
        }
        count /= 10
        if (total >= 10) {
            while (i != 0) {
                if (numberOfDec == n) return cloneTotal / count
                ++numberOfDec
                cloneTotal %= count
                count /= 10
                --i
            }
            ++k
        } else {
            if (numberOfDec == n) return cloneTotal
            ++k
            ++numberOfDec
        }
    }
}
