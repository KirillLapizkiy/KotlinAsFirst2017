@file:Suppress("UNUSED_PARAMETER")
package lesson6.task1

import com.sun.corba.se.impl.oa.poa.POAImpl
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
    fun farthest(vararg points: Point): Point {
        var farthestPoint = points[0]
        var theBiggestDistance = points[0].distance(Point(x,y))
        for (point in points){
            if (point.distance(Point(x,y)) > theBiggestDistance) {
                farthestPoint = point
                theBiggestDistance = point.distance(Point(x,y))
            }
        }
        return farthestPoint
    }
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
    fun farthest(vararg points: Point): Point {
        var farthestPoint = points[0]
        var theBiggestDistance = points[0].distance(center)
        for (point in points){
            if (point.distance(center) > theBiggestDistance) {
                farthestPoint = point
                theBiggestDistance = point.distance(center)
            }
        }
        return farthestPoint
    }
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    // Позиция точки относительно отрезка ( справа(<0), слева(>=0) )
    fun rotate(p: Point) = (end.x - begin.x) * (p.y - end.y) - (end.y - begin.y) * (p.x - end.x)
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
        val angleImproved = when {
            (angle >= PI) -> angle - PI
            (angle < 0.0) -> angle + PI
            else -> angle
        }
        val otherAngleImproved = when {
            (other.angle >= PI) -> other.angle - PI
            (other.angle < 0.0) -> other.angle + PI
            else -> other.angle
        }
        val k1 = tan(angleImproved)
        val k2 = tan(otherAngleImproved)
        val cosAngle = cos(angleImproved)
        val sinAngle = sin(angleImproved)
        val otherCosAngle = cos(otherAngleImproved)
        val otherSinAngle = sin(otherAngleImproved)
        if (angleImproved == PI / 2) return Point(-b, -b * otherSinAngle / otherCosAngle + other.b / otherCosAngle)
        if (otherAngleImproved == PI / 2) return Point(-other.b, -other.b * sinAngle / cosAngle + b / cosAngle)
        val x = (other.b / otherCosAngle - b / cosAngle) / (k1 - k2)
        val y = x * k2 + other.b / otherCosAngle
        return Point(x, y)
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
    val k = (s.end.y - s.begin.y) / (s.end.x - s.begin.x)
    var angle = atan(k)
    if (angle >= PI) angle -= PI else
    if (angle < 0.0) angle += PI
    //отказался от abs, потому что с ним значения не точны
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
    var angle = lineByPoints(a, b).angle + (PI / 2)  //угол, плюс перпендикуляр, т.к. основа может быть наклонена
    if (angle >= PI) angle -= PI else
        if (angle < 0.0) angle += PI
    //отказался от abs, потому что с ним значения не точны
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
    for (i in 0 until circles.count()/2) {
        for (j in circles.count()/2 until circles.count()) {
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
/*fun grahamscan(vararg points: Point): List<Point>{
    val pointsList = listOf(*points).distinct().toMutableList()
    var minPoint = pointsList[0]
    var minPointIndex = 0

    for(i in 1 until pointsList.size)
        if (pointsList[i].x < minPoint.x) {
            minPoint = pointsList[i]
            minPointIndex = i
        }
    pointsList[minPointIndex] = pointsList[0]
    pointsList[0] = minPoint
    return pointsList.toList()
}
fun swap(points: MutableList<Point>, swapCoordinate1: Int, swapCoordinate2: Int){
    var tempCoordinate = swapCoordinate1
    val tempValue = points[swapCoordinate1]
    points[tempCoordinate] = points[swapCoordinate2]
    points[swapCoordinate2] = tempValue
}
when {
    points.isEmpty() -> throw IllegalArgumentException()
    points.size == 1 -> return Circle(points[0], 0.0)
    points.size == 2 -> return circleByDiameter(Segment(points[0], points[1]))
}
val grahamScannedPoints = grahamscan(*points).toMutableList()
for (i in 2 until grahamScannedPoints.size) {
    var j = i
    while ((j > 1) && (Segment(grahamScannedPoints[0], grahamScannedPoints[j - 1]).rotate(grahamScannedPoints[j]) < 0)){
        swap(grahamScannedPoints, j, j - 1)
        j -= 1
    }

}
val MCH = mutableListOf<Point>(grahamScannedPoints[0], grahamScannedPoints[1])
for (i in 2 until grahamScannedPoints.size) {
    while (Segment(MCH[MCH.size - 2], MCH[MCH.size - 1]).rotate(grahamScannedPoints[i]) < 0)
        MCH.removeAt(MCH.size - 1)
    MCH.add(grahamScannedPoints[i])
}
return Circle(diameter(MCH), )*/

fun minContainingCircle(vararg points: Point): Circle = TODO()
/*{
    val d = diameter(*points)
    if ((d.begin.x == d.end.x) && (d.begin.y == d.end.y)) return circleByDiameter(Segment(points[0], points[1]))
    var maxCircle = circleByDiameter(d) //стартовая окружность
    var storedRadius = maxCircle.radius //если радиус не изменится во время выполнения цикла, цикл нужно прервать
    val fixedPoints = mutableListOf<Point>(d.begin, d.end) // опорные точки
    while (true) {
        val theFarthestNewOne = maxCircle.farthest(*points) // самая дальняя точка от радиуса окружности, она же - "новая"
        if ((theFarthestNewOne.distance(maxCircle.center) <= maxCircle.radius) ||
                (theFarthestNewOne in fixedPoints) ||
                (theFarthestNewOne in fixedPoints)) break
        val theFarthestFromTheNewOne = theFarthestNewOne.farthest(*fixedPoints.toTypedArray())   // самая дальняя точка от "новой"
        val unusedFixedPoints = mutableListOf<Point>() // для этого списка найдём опорные точки (если их было 2-3 в fixedlist), которые не оказались самыми дальними от новой
        val unusedFixedPointsIndexes = mutableListOf<Int>() // индексы этих неиспользованных точек, чтобы их можно было легче удалить
        for (point in fixedPoints)  // поиск неиспользованных точек, тех точек, что не являются самыми дальними от "новой" точки
            if (!point.equals(theFarthestFromTheNewOne)) {
                unusedFixedPoints.add(point) //если точка не самая дальняя от новой, значит она - неиспользованная
                unusedFixedPointsIndexes.add(fixedPoints.indexOf(point)) //индекс неиспользованной тоже добавим
            }
        maxCircle = circleByDiameter(Segment(theFarthestFromTheNewOne, theFarthestNewOne)) // круг, построенный по самой дальней опорной точке и той новой точке
        if (fixedPoints.size == 3) {
            val theFarthestUnusedFromTheNewOne = maxCircle.farthest(*unusedFixedPoints.toTypedArray()) //Находим из двух неиспользованных точек самую удаленную от центра текущей окружности.
            if (!maxCircle.contains(theFarthestUnusedFromTheNewOne))
                maxCircle = circleByThreePoints(theFarthestNewOne,
                        theFarthestFromTheNewOne, theFarthestUnusedFromTheNewOne)
            else
                maxCircle = circleByDiameter(Segment(theFarthestNewOne, theFarthestFromTheNewOne))
        }
        else
            if (fixedPoints.size == 2) {
                if (maxCircle.contains(unusedFixedPoints[0])) { //если неиспользованная точка не выходит за пределы окружности, заменить её опорной, что самая дальняя от "новой"
                    fixedPoints.removeAt(unusedFixedPointsIndexes[0]) //(индекс неиспользованной пригодился)
                    fixedPoints.add(theFarthestFromTheNewOne)
                }
                else { //иначе построить окружность по "новой" и двум опорным точкам
                    fixedPoints.add(theFarthestFromTheNewOne)
                    maxCircle = circleByThreePoints(fixedPoints[0], fixedPoints[1], theFarthestNewOne)
                    if (maxCircle.radius <= storedRadius) break else storedRadius = maxCircle.radius
                    // если радиус не изменился, завершить цикл
                }

            }
    }
    return maxCircle
}*/