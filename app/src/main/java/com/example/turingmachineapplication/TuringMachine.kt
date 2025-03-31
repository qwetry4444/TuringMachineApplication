package com.example.turingmachineapplication

class TuringMachine(
    private val tape: MutableList<Char> = mutableListOf('0', '0', '0')
) {
    private val readHead: ReadHead = ReadHead(0)
    private var state: State = State.q1

    fun getTape() = tape
    fun getHeadPosition() = readHead.getHeadPosition()

    fun step(): Boolean {
        if (state == State.q0) return true

        val currentSymbol = readHead.read(tape)

        when(state) {
            State.q1 -> {
                when(currentSymbol) {
                    '0' -> readHead.moveRight()
                    '1' -> {
                        state = State.FirstOperand
                        readHead.moveRight()
                    }
                    else -> state = State.q0
                }
            }

            State.FirstOperand -> {
                when (currentSymbol) {
                    '+' -> {
                        state = State.SecondOperand
                        readHead.write('1', tape)
                        readHead.moveRight()
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
                return true
            }
        }
        return false
    }
}

class ReadHead(
    private var headPosition: Int
) {
    fun moveRight() { headPosition += 1}
    fun moveLeft() { headPosition -= 1 }

    fun write(character: Char, tape: MutableList<Char>) {
        if (headPosition in 0..tape.size)
            tape[headPosition] = character
        else
            tape.add(headPosition, character)
    }

    fun read(tape: MutableList<Char>): Char {
        if (headPosition in 0..tape.size)
            return tape[headPosition]
        return '0'
    }

    fun getHeadPosition() = headPosition
}

enum class State {
    q0, q1, FirstOperand, SecondOperand
}