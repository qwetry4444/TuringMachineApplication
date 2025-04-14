package com.example.turingmachineapplication

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
                        state = State.ReduceSecondOperand
                        readHead.moveRight()
                    }
                    '1' -> readHead.moveRight()
                    else -> state = State.q0
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

    private var carry = 0 // перенос

    private fun stepAdditionBinary(currentSymbol: Char): Boolean {
        when (state) {
            State.q1 -> {
                // Идем вправо к знаку '='
                when (currentSymbol) {
                    '0', '1', '+', -> readHead.moveRight()
                    '=' -> {
                        state = State.SumBits
                        readHead.moveLeft()
                    }
                    else -> state = State.q0
                }
            }

            State.SumBits -> {
                // Читаем по одному биту с конца A и B
                val pos = readHead.getHeadPosition()
                val bitB = readHead.read(tape)
                readHead.write(' ', tape) // очищаем текущий бит
                readHead.moveLeft()

                // Найти бит A
                var bitA = '0'
                while (readHead.read(tape) != '+') {
                    readHead.moveLeft()
                }
                readHead.moveLeft()
                bitA = readHead.read(tape)
                readHead.write(' ', tape) // очищаем

                // Сумма + перенос
                val a = if (bitA == '1') 1 else 0
                val b = if (bitB == '1') 1 else 0
                val sum = a + b + carry

                carry = if (sum >= 2) 1 else 0
                val resultBit = if (sum % 2 == 1) '1' else '0'

                // Идём к знаку '=' и пишем результат
                while (readHead.read(tape) != '=') {
                    readHead.moveRight()
                }
                readHead.moveRight()
                readHead.write(resultBit, tape)

                // Вернуться назад
                state = State.MoveToNext
            }

            State.MoveToNext -> {
                // Проверка, достигнут ли левый конец
                if (readHead.getHeadPosition() <= 0) {
                    if (carry == 1) {
                        tape.add('1') // пишем перенос
                    }
                    state = State.q0
                    return true
                }
                readHead.moveLeft()
                state = State.SumBits
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
    GoToFirstOperand, ReduceFirstOperand, ReduceSecondOperand,
    AddOneToResult, RestoreSecondOperand, GoToResult, GoToSecondOperand,
    MoveToEnd, SumBits
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

// xdd