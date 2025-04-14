package com.example.turingmachineapplication.core.domain.TurginMachineLogic

import kotlinx.serialization.Serializable

class TuringMachine(
    private val tape: MutableList<Char> = mutableListOf(),
    private var currentAlgorithm: Algorithm = Algorithm.AdditionUnary
) {
    private val readHead: ReadHead = ReadHead(0, tape)
    private var state: State = State.q1


    fun getTape() = tape
    fun getHeadPosition() = readHead.getHeadPosition()

    fun step(): Boolean {
        if (state == State.q0) return true

        val currentSymbol = readHead.read()

        return when (currentAlgorithm) {
            Algorithm.AdditionUnary -> {
                stepAdditionUnary(currentSymbol)
            }
            Algorithm.MultiplicationUnary -> {
                stepMultiplicationUnary(currentSymbol)
            }
            Algorithm.AdditionBinary -> {
                stepAdditionBinary(currentSymbol)
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
                        readHead.write('1')
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
                        readHead.write('0')
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
                        state = State.AddEqu
                        readHead.moveRight()
                    }
                    '1' -> readHead.moveRight()
                    else -> state = State.q0
                }
            }

            State.AddEqu -> {
                when (currentSymbol) {
                    '1' -> readHead.moveRight()
                    '0' -> {
                        readHead.write('=')
                        readHead.moveLeft()
                        state = State.ReduceSecondOperand
                    }
                }
            }

            State.ReduceSecondOperand -> {
                when (currentSymbol) {
                    '1' -> {
                        readHead.write('a')
                        readHead.moveLeft()
                        state = State.GoToFirstOperand
                    }
                    '=' -> {
                        readHead.moveLeft()
                        state = State.RestoreSecondOperand
                    }
                    'a' -> readHead.moveRight()
                    else -> state = State.q0
                }
            }
            State.RestoreSecondOperand -> {
                when (currentSymbol){
                    'a' -> {
                        readHead.write('1')
                        readHead.moveLeft()
                    }
                    else -> state = State.q0
                }
            }


            State.GoToFirstOperand -> {
                when (currentSymbol) {
                    '1', '=', 'a' -> readHead.moveLeft()
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
                        readHead.write('a')
                        state = State.GoToResult
                        readHead.moveRight()
                    }
                    '0' -> {
                        state = State.GoToSecondOperand
                        readHead.moveRight()
                    }
                    else -> state = State.q0
                }
            }

            State.GoToSecondOperand -> {
                when(currentSymbol){
                    '*' -> {
                        readHead.moveRight()
                        state = State.ReduceSecondOperand
                    }
                    'a' -> {
                        readHead.write('1')
                        readHead.moveRight()
                    }
                    else -> state = State.q0
                }
            }

            State.GoToResult -> {
                when (currentSymbol) {
                    '=' -> {
                        state = State.AddOneToResult
                        readHead.moveRight()
                    }
                    'a', '1', '*' -> readHead.moveRight()
                    else -> state = State.q0
                }
            }

            State.AddOneToResult -> {
                when (currentSymbol) {
                    '1' -> readHead.moveRight()
                    '0' -> {
                        readHead.write('1')
                        state = State.GoToFirstOperand
                        readHead.moveLeft()
                    }
                    else -> state = State.q0
                }
            }
            State.q0 -> return true

            else -> state = State.q0

        }
        return false
    }


    private fun stepAdditionBinary(currentSymbol: Char): Boolean {
        when (state) {
            State.q1 -> {
                when (currentSymbol) {
                    '0', '1' -> readHead.moveRight()
                    '+' -> {
                        state = State.SeekRightMostBitSecond
                        readHead.moveRight()
                    }
                    else -> state = State.q0
                }
            }

            State.SeekRightMostBitSecond -> {
                when (currentSymbol) {
                    '0', '1' -> readHead.moveRight()
                    '=' -> {
                        readHead.moveLeft()
                        state = State.DecrementSecond
                    }
                    else -> state = State.q0
                }
            }

            State.DecrementSecond -> {
                when (currentSymbol) {
                    '1' -> {
                        readHead.write('0')
                        state = State.MoveToResult
                        readHead.moveLeft()
                    }
                    '0' -> {
                        readHead.write('1')
                        readHead.moveLeft()
                    }
                    '+' -> {
                        // второй операнд стёрт — завершение
                        state = State.q0
                    }
                    else -> state = State.q0
                }
            }

            State.MoveToResult -> {
                when (currentSymbol) {
                    '0', '1' -> readHead.moveLeft()
                    '+' -> {
                        state = State.IncrementFirst
                        readHead.moveLeft()
                    }
                    else -> state = State.q0
                }
            }

            State.IncrementFirst -> {
                when (currentSymbol) {
                    '0' -> {
                        readHead.write('1')
                        state = State.q1
                        readHead.moveRight()
                    }
                    '1' -> {
                        readHead.write('0')
                        readHead.moveLeft()
                    }
                    else -> {
                        // если дошли до левой границы
                        readHead.write('1')
                        state = State.q1
                        readHead.moveRight()
                    }
                }
            }

            State.q0 -> return true
            else -> state = State.q0
        }
        return false
    }


}



enum class State {
    q0, q1,
    FirstOperand, SecondOperand,
    GoToFirstOperand, ReduceFirstOperand, ReduceSecondOperand, AddEqu,
    AddOneToResult, RestoreSecondOperand, GoToResult, GoToSecondOperand,
    SeekRightMostBitSecond,
    DecrementSecond,
    MoveToResult,
    IncrementFirst

}


class ReadHead(
    private var headPosition: Int,
    private var tape: MutableList<Char> = mutableListOf()
) {
    fun moveRight() {
        headPosition += 1
        if (headPosition == tape.size)
            tape.add('0')
    }
    fun moveLeft() {
        headPosition -= 1
        if (headPosition <= 0)
            tape.add(0, '0')
    }

    fun write(character: Char) {
        if (headPosition in 0..<tape.size)
            tape[headPosition] = character
        else
            tape.add(character)
    }

    fun read(): Char {
        if (headPosition in 0..<tape.size)
            return tape[headPosition]
        return 'l'
    }

    fun getHeadPosition() = headPosition
}


@Serializable
enum class Algorithm {
    AdditionUnary,
    MultiplicationUnary,
    AdditionBinary;

    companion object {
        fun toString(algorithm: Algorithm): String {
            return when(algorithm){
                AdditionUnary -> "Сложение унарных чисел"
                MultiplicationUnary -> "Умножение унарных чисел"
                AdditionBinary -> "Сложение бинарных чисел"
            }
        }
    }
}