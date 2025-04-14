package com.example.turingmachineapplication.core.domain.TurginMachineLogic

import kotlinx.serialization.Serializable

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
                        readHead.write('=', tape)
                        readHead.moveLeft()
                        state = State.ReduceSecondOperand
                    }
                }
            }

            State.ReduceSecondOperand -> {
                when (currentSymbol) {
                    '1' -> {
                        readHead.write('a', tape)
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
                        readHead.write('1', tape)
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
                        readHead.write('a', tape)
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
                        readHead.write('1', tape)
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
                        readHead.write('1', tape)
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


    //111ba+1aa=111
    private var carry = 0
    private fun stepAdditionBinary(currentSymbol: Char): Boolean {
        when (state) {
            State.q1 -> {
                when (currentSymbol) {
                    '0', '1' -> {
                        readHead.moveRight()
                    }
                    '+' -> {
                        state = State.FindEnd
                        readHead.moveRight()
                    }
                    else -> state = State.q0
                }
            }

            State.FindEnd -> {
                when (currentSymbol) {
                    '0', '1' -> readHead.moveRight()
                    '=' -> {
                        state = State.BackToLast
                        readHead.moveLeft()
                    }
                    else -> state = State.q0
                }
            }

            State.BackToLast -> {
                when (currentSymbol) {
                    '0', '1' -> {
                        state = State.AddBits
                        //readHead.moveLeft()
                    }
                    '+' -> {
                        state = State.HandleCarry
                        readHead.moveLeft()
                    }
                    else -> state = State.q0
                }
            }

            State.AddBits -> {
                val bitB = currentSymbol
                readHead.write('a', tape) // Помечаем обработанный бит
                readHead.moveLeft()

                // Ищем бит первого числа
                while (readHead.read(tape) != '+') {
                    readHead.moveLeft()
                }
                readHead.moveLeft()
                while (!setOf('1', '0').contains(readHead.read(tape)))
                    readHead.moveLeft()

                val bitA = readHead.read(tape)
                readHead.write('a', tape) // Помечаем обработанный бит

                // Вычисляем сумму
                val sum = (if (bitA == '1') 1 else 0) +
                        (if (bitB == '1') 1 else 0) + carry
                carry = if (sum >= 2) 1 else 0
                val resultBit = if (sum % 2 == 1) '1' else '0'

                // Идем к концу, чтобы записать результат
                while (readHead.read(tape) != 'a') {
                    readHead.moveRight()
                }
                readHead.write(resultBit, tape)

                state = State.BackToLast
            }

            State.HandleCarry -> {
                if (carry == 1) {
                    // Добавляем перенос в начало
                    tape.add(0, '1')
                    //readHead.reset(0)
                }
                state = State.Halt
            }
            State.Halt -> return false

            //State.q1 -> return true

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
    FindEnd, BackToLast, AddBits, HandleCarry, Halt
}


class ReadHead(
    private var headPosition: Int
) {
    fun moveRight() { headPosition += 1}
    fun moveLeft() { headPosition -= 1 }

    fun write(character: Char, tape: MutableList<Char>) {
        if (headPosition in 0..<tape.size)
            tape[headPosition] = character
        else
            tape.add(character)
    }

    fun read(tape: MutableList<Char>): Char {
        if (headPosition in 0..<tape.size)
            return tape[headPosition]
        return '0'
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