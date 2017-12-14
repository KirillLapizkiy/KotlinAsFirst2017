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
    override fun equals(other: Any?) =
            other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
            begin.hashCode() + end.hashCode()

}

/*
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
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
    var angle = if (s.begin.x == s.end.x) PI/2 else atan(k)
    if (angle > Math.PI) angle -= Math.PI else
        if (angle < 0.0) angle += Math.PI
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
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw  IllegalArgumentException()
    var minDistance = circles[0].distance(circles[1])
    var pair = Pair(circles[0], circles[1])
    for (i in 0 until circles.count()) {
        for (j in 0 until circles.count()) {
            val distance = circles[i].distance(circles[j])
            if (i != j) {
                if (distance < minDistance) {
                    pair = Pair(circles[i], circles[j])
                    minDistance = distance
                }
            }
        }
    }
    return pair
}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val toCenter = bisectorByPoints(a, b).crossPoint(bisectorByPoints(b, c))
    return Circle(toCenter, toCenter.distance(b))
}

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
fun minContainingCircle(vararg points: Point): Circle {
    when{
        points.isEmpty() -> throw IllegalArgumentException()
        points.size == 1 -> return Circle(points[0], 0.0)
    }
    val pointsList = points.toList()
    var prevD = circleByDiameter(Segment(pointsList[0],pointsList[1]))
    var d: Circle
    for (i in 3..pointsList.count()){
        if(prevD.contains(pointsList[i])) d = prevD else
            d = MinDiscWithPoint(*points, points[i])
    }
}
fun MinDiscWithPoint (vararg points: Point, pi: Point): Circle{
    var prevD = circleByDiameter(Segment(points[0], pi))
    for (i in 2..points.count()){
        if(prevD.contains(points[i])) d = prevD else
            d = MinDiscWithPoint(*points, points[i])
    }
}
