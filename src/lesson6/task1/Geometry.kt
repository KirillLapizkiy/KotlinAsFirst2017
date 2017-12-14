@file:Suppress("UNUSED_PARAMETER")
package lesson6.task1

import lesson1.task1.sqr
import java.lang.Math.*

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = Math.sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point): this(linkedSetOf(a, b, c))
    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return Math.sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */

    fun distance(other: Circle): Double {
        val centerDistance = sqrt(sqr(center.x - other.center.x) + sqr(center.y - other.center.y))
        val radiusSum = radius + other.radius
        if (centerDistance > radiusSum) return centerDistance - radiusSum
        return 0.0
    }
    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = sqrt(sqr(center.x - p.x) + sqr(center.y - p.y)) <= radius

}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    //определяет, с какой стороны от вектора AB находится точка C
    fun rotate(p: Point) = (end.x - begin.x) * (p.y - end.y) - (end.y - begin.y) * (p.x - end.x)

    // Середина отрезка (точка)
    fun mid(): Point{
        val x = begin.x + (end.x - begin.x) / 2
        val y = begin.y + (end.y - begin.y) / 2
        return Point(x, y)
    }

    // Длина отрезка
    fun len() = begin.distance(end)
    override fun equals(other: Any?) =
            other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
            begin.hashCode() + end.hashCode()

}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
/*fun diameter(vararg points: Point): Segment {
    val pointsList = points.toList()
    if (pointsList.count() < 2) throw IllegalArgumentException()
    var theFarthest = 0.0
    var result = Segment(pointsList[0], pointsList[1])
    for (i in 0 until pointsList.count() - 1){
        for (j in i+1 until pointsList.count()){
            val someSegmentLength = sqrt(sqr(pointsList[i].x - pointsList[j].x) +
                                         sqr(pointsList[i].y - pointsList[j].y))
            if (someSegmentLength > theFarthest) {
                theFarthest = someSegmentLength
                result = Segment(pointsList[i],pointsList[j])
            }
        }
    }
    return result
}*/
fun main(args: Array<String>){
    val p1 = Point(0.0, 0.0)
    val p2 = Point(1.0, 4.0)
    val p3 = Point(-2.0, 2.0)
    val p4 = Point(3.0, -1.0)
    val p5 = Point(-3.0, -2.0)
    val p6 = Point(0.0, 5.0)
    val res = diameter(p1, p2, p3, p4)
    println("RES: ${res.begin}  ${res.end}") // (-2;2) (3;-1)
}

fun diameter(vararg points: Point): Segment {
    val mch = MCH(*points)
    mch.printVertices()
    return mch.diameter()
}

/**
 * Минимальная выпуклая оболочка (МВО) множества точек плоскости
 * (многоукольник, описывающий множество)
 */
class MCH constructor(vararg points: Point) {
    private var vertices = listOf(*points) // список вершин МВО
    // (становится таковым после выполнения конструктора)

    /**
     * Алгоритм Джарвиса для построения МВО
     */
    init {
        if(vertices.size < 3) throw IllegalArgumentException("less than two arguments passed")
        setFirstPoint() // шаг 1 - определение отправной точки
        building() // шаг 2 - построение МВО
    }

    private fun setFirstPoint() {
        var result = vertices.toMutableList() //перепишем список всех точек в мутирующий список
        var min_x_point = result[0] //берём первую точку, чтобы было с чем сравнивать
        var min_x_index = 0 //индекс точки с минимальным x

        for(i in 1 until result.size)
            if (result[i].x < min_x_point.x) {
                min_x_point = result[i]
                min_x_index = i
            }
        result[min_x_index] = result[0]
        result[0] = min_x_point
        vertices = result.toList()
    }

    /**
     *  Построение МВО
     */
    private fun building() {
        val temp = vertices.toMutableList()
        val result = mutableListOf(temp[0]) // инициализация первой точкой мн-ва (она точно прин. МВО)
        temp.removeAt(0) // удаление первой точки из обрабатываемого списка
        temp.add(result[0]) // и её добавление в конец
        var next_point: Int
        var seg: Segment

        // поиск вершин МВО (против ч.с.)
        // след. вершиной МВО становится "самая правая" точка по отношению к текущей
        while(true) {
            next_point = 0
            if(temp.isEmpty()) break // (на случай, если все точки мн-ва составляют МВО)
            for(i in 1 until temp.size) {
                seg = Segment(result.last(), temp[next_point])
                if (seg.rotate(temp[i]) < 0.0) next_point = i
            }
            if(temp[next_point] == result[0]) break // если вернулись к стартовой точке
            result.add(temp[next_point]) // запись точки в result
            temp.removeAt(next_point) // удаление точки из обрабатываемого списка


        }
        vertices = result.toList() // запись результата
        /*for (i in 2 until result.size)){               //сортировка
            j = i
            while j > 1 and (rotate(A[P[0]], A[P[j - 1]], A[P[j]]) < 0):
                P[j], P[j-1] = P[j-1], P[j]
                j -= 1
        }*/
    }

    fun diameter(): Segment {
        var i = 0
        var j = 1
        var seg1: Segment
        var seg2: Segment
        var max_seg = Segment(Point(0.0,0.0), Point(0.0,0.0)) // нулевой отрезок
        var S = 0.0 // S текущего тре-ника

        // Поиск наиболее удаленной от "стартового" отрезка точки (через S тре-ника)
        while(Triangle(vertices[i], vertices[i+1], vertices[j+1]).area() > S){
            S = Triangle(vertices[i], vertices[i+1], vertices[j+1]).area()
            j++
            if(j+1 >= vertices.size) break
        }

        // Поиск диаметра
        while(j < vertices.size) { // пока q не дошло до p0
            seg1 = Segment(vertices[i], vertices[j])
            seg2 = Segment(vertices[i+1], vertices[j])
            max_seg = maxSeg(seg1, seg2, max_seg)
            j++
            i++
        }
        return max_seg
    }

    // Отрезок наибольшей длины
    fun maxSeg(vararg segments: Segment): Segment {
        var index_of_max = 0
        var max_len = segments[0].len()
        for(i in 1 until segments.size)
            if(segments[i].len() > max_len){
                max_len = segments[i].len()
                index_of_max = i
            }
        return segments[index_of_max]
    }

    fun printVertices(){
        for(p in vertices)
            println("${p.x}\t${p.y}")
    }
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle =
        Circle(Point(((diameter.begin.x + diameter.end.x) / 2), ((diameter.begin.y + diameter.end.y) / 2)),
        (diameter.begin.distance(diameter.end))/2)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        assert(angle >= 0 && angle < Math.PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double): this(point.y * Math.cos(angle) - point.x * Math.sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val sinAlpha = sin(angle)
        val cosAlpha = cos(angle)
        val sinBeta = sin(other.angle)
        val cosBeta = cos(other.angle)
        if (sinAlpha * cosBeta == sinBeta * cosAlpha) throw IllegalArgumentException()
        val y = (-b * sinBeta + other.b * sinAlpha) /
                (sinAlpha * cosBeta * (1 - (sinBeta * cosAlpha)
                        / (sinAlpha * cosBeta)))
        val x = (y * cosAlpha - b) / sinAlpha
        return Point(x,y)

    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${Math.cos(angle)} * y = ${Math.sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line {
    val b = (s.end.y * s.begin.x - (s.begin.y * s.end.x)) / ((1 - s.end.x / s.begin.x) * s.begin.x)
    val k = if (s.begin.y != s.end.y) (s.begin.y - b) / s.begin.x else 0.0
    val angle = if (s.begin.x == s.end.x) PI/2 else atan(k)
    return Line(s.begin, angle)
}

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val line = lineByPoints(a, b)
    var angle = Math.PI / 2 + line.angle
    if (abs(angle) >= Math.PI) angle = abs(angle % Math.PI)
    val point = Point((b.x + a.x) / 2.0, (b.y + a.y) / 2.0)
    return Line(point, angle)
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> = TODO()

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle = TODO()

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle = TODO()

