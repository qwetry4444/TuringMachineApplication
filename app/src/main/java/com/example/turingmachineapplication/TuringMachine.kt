package com.example.turingmachineapplication

class TuringMachine(
    private val tape: MutableList<Char> = mutableListOf('0', '0', '0')
) {
    private val readHead: ReadHead = ReadHead(0)
    private var state: State = State.q1
    private var isRunning = false

    fun getCurrentState() = state
    fun getTape() = tape.toList()
    fun getHeadPosition() = readHead.getCurrentPosition()
    fun isMachineRunning() = isRunning

    fun step(): Boolean {
        if (state == State.q0) return false

        isRunning = true
        val currentSymbol = readHead.read(tape)

        when(state) {
            State.q1 -> {
                when(currentSymbol) {
                    '0' -> readHead.moveRight()
                    '1' -> state = State.FirstOperand
                    else -> state = State.q0
                }
            }

            State.FirstOperand -> {
                when (currentSymbol) {
                    '+' -> {
                        state = State.SecondOperand
                        readHead.write('1', tape)
                    }

                    '1' -> readHead.moveRight()
                    else -> state = State.q0
                }
            }

            State.SecondOperand -> {
                when(currentSymbol) {
                    '1' -> readHead.moveRight()
                    '0' -> {
                        readHead.moveLeft()
                        readHead.write('0', tape)
                        state = State.q0
                    }
                    else -> state = State.q0
                }
            }

            State.q0 -> {
                isRunning = false
                return false
            }
        }
        return true
    }

}

class ReadHead(
    private var currentPosition: Int
) {
    fun moveRight() { currentPosition += 1}
    fun moveLeft() { currentPosition -= 1 }

    fun write(character: Char, tape: MutableList<Char>) {
        if (currentPosition in 0..tape.size)
            tape[currentPosition] = character
        else
            tape.add(currentPosition, character)
    }

    fun read(tape: MutableList<Char>): Char {
        if (currentPosition in 0..tape.size)
            return tape[currentPosition]
        return '0'
    }

    fun getCurrentPosition() = currentPosition
}

enum class State {
    q0, q1, FirstOperand, SecondOperand
}