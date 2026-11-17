fun readCoordinate(): Pair<Int, Int>? {
    println("Введите координаты (row col):")
    val input = readln().trim().split(Regex("\\s+"))
    if (input.size != 2) {
        println("Ошибка: введите два числа через пробел.")
        return null
    }
    val r = input[0].toIntOrNull()
    val c = input[1].toIntOrNull()
    if (r == null || c == null) {
        println("Ошибка: координаты должны быть числами.")
        return null
    }
    return Pair(r, c)
}
fun placePlayerShips(player: Player) {
    val plan = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    var index = 0
    while (index < plan.size) {
        val expectedSize = plan[index]
        println()
        println("Ставим корабль №${index + 1} из ${plan.size} (${expectedSize} палубы):")
        printField(player, false)
        println("Введите начальную координату (row col):")
        val coords = readCoordinate()
        if (coords == null) continue
        val (startRow, startCol) = coords
        println("Введите тип корабля (1, 2, 3 или 4):")
        val size = readln().trim().toIntOrNull()
        if (size == null || size !in 1..4) {
            println("Ошибка: тип корабля должен быть 1, 2, 3 или 4.")
            continue
        }
        if (size != expectedSize) {
            println("Сейчас нужно ставить корабль на $expectedSize палубы, а не $size.")
            continue
        }
        var horizontal = true
        if (size > 1) {
            println("Введите направление (h — вправо, v — вниз):")
            val dir = readln().trim().lowercase()
            if (dir != "h" && dir != "v") {
                println("Ошибка: направление должно быть h или v.")
                continue
            }
            horizontal = (dir == "h")
        }
        val ok = player.placeShip(startRow, startCol, size, horizontal)
        if (ok) {
            println("Корабль поставлен!")
            index++
        } else {
            println("Нельзя поставить корабль здесь. Попробуйте снова.")
        }
    }
    println()
    println("Все корабли расставлены!")
    printField(player, false)
}
fun placeEnemyShips(enemy: Player) {
    val plan = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1).shuffled()
    val maxResets = 100
    var resets = 0
    var index = 0
    while (index < plan.size) {
        val size = plan[index]
        var placed = false
        val cells = mutableListOf<Pair<Int, Int>>()
        for (r in 0..9) {
            for (c in 0..9) {
                cells.add(Pair(r, c))
            }
        }
        cells.shuffle()
        for (cell in cells) {
            val r = cell.first
            val c = cell.second
            if (!placed && enemy.placeShip(r, c, size, true)) {
                placed = true
            }
            if (!placed && enemy.placeShip(r, c, size, false)) {
                placed = true
            }
        }
        if (placed) {
            index++
        } else {
            resets++
            if (resets > maxResets) {
                println("Не удалось расставить корабли компьютера за $maxResets попыток.")
                return
            }
            enemy.reset()
            index = 0
        }
    }
    println("Компьютер расставил корабли.")
}