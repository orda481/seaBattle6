fun isGameOver(player: Player, enemy: Player): Boolean {
    return !player.hasShips() || !enemy.hasShips()
}
fun main() {
    printRules()
    val player = Player("Вы")
    val enemy = Player("Компьютер")
    println()
    println("Выберите уровень сложности компьютера:")
    val difficulty = when (readln().trim()) {
        "1" -> Difficulty.EASY
        "2" -> Difficulty.MEDIUM
        else -> Difficulty.HARD
    }
    println("=== РАССТАНОВКА ВАШИХ КОРАБЛЕЙ ===")
    placePlayerShips(player)
    println()
    println("=== КОМПЬЮТЕР РАССТАВЛЯЕТ КОРАБЛИ ===")
    placeEnemyShips(enemy)
    println()
    println("=== ИГРА НАЧАЛАСЬ ===")
    val ai = EnemyAI(player, difficulty)
    var playerTurn = true
    var turns = 0
    while (!isGameOver(player, enemy)) {
        printBothFields(player, enemy)
        println()
        if (playerTurn) {
            val coords = readCoordinate()
            if (coords == null) continue
            val (r, c) = coords
            if (!enemy.isInside(r, c)) {
                println("Выстрел за границей поля! Попробуйте снова.")
                continue
            }
            val currentState = enemy.field()[r][c]
            if (currentState == CellState.HIT || currentState == CellState.MISS) {
                println("Сюда уже стреляли! Попробуйте снова.")
                continue
            }
            val hit = enemy.shoot(r, c)
            turns++
            if (hit) {
                println("Вы попали!")
            } else {
                println("Вы промахнулись.")
                playerTurn = false
            }
        } else {
            println("Ход компьютера...")
            val (r,c) = ai.chooseShot()
            val hit = player.shoot(r, c)
            ai.onShotResult(r,c,hit)
            turns++
            if (hit) {
                println("Компьютер попал в ($r, $c)!")
            } else {
                println("Компьютер промахнулся в ($r, $c).")
                playerTurn = true
            }
        }
        println()
    }
    println("Компьютер потопил ${ai.countSunkShips()} кораблей.")
    println("Игра длилась $turns ходов.")
    if (enemy.hasShips()) {
        println("Победа компьютера!")
    } else {
        println("Вы победили!")
    }
    ai.reset()
    println("Состояние ИИ сброшено. Можно начинать новую игру.")
}