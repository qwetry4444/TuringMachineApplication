package com.example.turingmachineapplication

class TuringMachine(
    private val tape: MutableList<Char> = mutableListOf(),
    private var currentAlgorithm: Algorithm = Algorithm.AdditionUnary
) {
    private val readHead: ReadHead = ReadHead(0)
    private var state: State = State.q1


    fun getTape() = tape
    fun getHeadPosition() = readHead.getHeadPosition()

    fun step(): Boolean {
        if (state == State.q0) return true

        val currentSymbol = readHead.read(tape)

        return when (currentAlgorithm) {
            Algorithm.AdditionUnary -> {
                stepAdditionUnary(currentSymbol)
            }
            Algorithm.MultiplicationUnary -> {
                stepMultiplicationUnary(currentSymbol)
            }

            else -> false
        }
    }

    private fun stepAdditionUnary(currentSymbol: Char): Boolean {
        when (state) {
            State.q1 -> {
                when (currentSymbol) {
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
                when (currentSymbol) {
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

            else -> return false
        }
        return false
    }

    private fun stepMultiplicationUnary(currentSymbol: Char): Boolean {
        when (state) {
            State.q1 -> {
                when (currentSymbol) {
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
                    '*' -> {
                        state = State.SecondOperand
                        readHead.moveRight()
                    }

                    '1' -> readHead.moveRight()
                    else -> state = State.q0
                }
            }

            State.SecondOperand -> {
                when (currentSymbol) {
                    '1' -> readHead.moveRight()
                    '0' -> {
                        readHead.write('=', tape)
                        readHead.moveLeft()
                        state = State.GoToFirstOperand
                    }
                    else -> state = State.q0
                }
            }

            State.GoToFirstOperand -> {
                when (currentSymbol) {
                    '1' -> readHead.moveLeft()
                    '=' -> readHead.moveLeft()
                    '*' -> {
                        state = State.ReduceFirstOperand
                        readHead.moveLeft()
                    }
                    else -> state = State.q0
                }
            }

            State.ReduceFirstOperand -> {
                when (currentSymbol) {
                    'a' -> readHead.moveLeft()
                    '1' -> {
                        readHead.write('a', tape)
                        state = State.GoToResult
                        readHead.moveRight()
                    }
                    '0' -> {
                        state = State.RestoreFirstOperand
                        readHead.moveRight()
                    }
                }
            }

            State.GoToResult -> {
                when (currentSymbol) {
                    '=' -> {
                        state = State.AddOneToResult
                        readHead.moveRight()
                    }
                    'a' -> readHead.moveRight()
                    '1' -> readHead.moveRight()
                    '*' -> readHead.moveRight()
                }
            }

            State.AddOneToResult -> {
                when (currentSymbol) {
                    '1' -> readHead.moveRight()
                    '0' -> {
                        readHead.write('1', tape)
                        state = State.GoToFirstOperand
                        readHead.moveLeft()
                    }
                }
            }


            State.StartFirstOperand -> {

            }
            State.StartSecondOperand -> TODO()
            State.GoToSecondOperand -> TODO()
            State.ReduceSecondOperand -> TODO()
            State.RestoreFirstOperand -> TODO()
            State.RestoreSecondOperand -> TODO()

            State.q0 -> return true

        }
        return false
    }
}





enum class State {
    q0, q1,
    FirstOperand, SecondOperand,
    StartFirstOperand, StartSecondOperand, GoToFirstOperand, GoToSecondOperand, ReduceFirstOperand, ReduceSecondOperand,
    AddOneToResult, RestoreFirstOperand, RestoreSecondOperand, GoToResult
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


enum class Algorithm {
    AdditionUnary,
    MultiplicationUnary,
    AdditionBinary
}