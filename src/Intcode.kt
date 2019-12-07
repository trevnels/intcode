class IntcodeRunner(val program: String){

    val stdin = mutableListOf<String>()
    val stdout = mutableListOf<Int>()
    var halted = false

    val memory = program.split(",").toMutableList()
    var inputCount = 0
    var currentInstruction = 0
    var op = 0

    fun step() {
        if(halted) return
        op = getOpcode(memory[currentInstruction])
        val modes = getModes(memory[currentInstruction])

        var jumped = false
//        println("op: $op, modes: $modes")
        when(op) {
            1 -> memory[memory[currentInstruction+3].toInt()] = (getParameter(memory, currentInstruction, 0, modes) + getParameter(memory, currentInstruction, 1, modes)).toString()
            2 -> memory[memory[currentInstruction+3].toInt()] = (getParameter(memory, currentInstruction,0, modes) * getParameter(memory, currentInstruction, 1, modes)).toString()
            3 -> memory[memory[currentInstruction+1].toInt()] = when {
                (stdin.size == 0) || (inputCount >= stdin.size) -> readLine()!!.toInt().toString()
                else -> stdin[inputCount++].toString()
            }
            4 -> {
                stdout += getParameter(memory, currentInstruction,0, modes)
            }
            5 -> {
                if(getParameter(memory, currentInstruction,0, modes) != 0) {
                    currentInstruction = getParameter(memory, currentInstruction,1, modes)
                    jumped = true
                }
            }
            6 -> {
                if(getParameter(memory, currentInstruction,0, modes) == 0) {
                    currentInstruction = getParameter(memory, currentInstruction,1, modes)
                    jumped = true
                }
            }
            7 -> memory[memory[currentInstruction+3].toInt()] = if(getParameter(memory, currentInstruction,0, modes) < getParameter(memory, currentInstruction,1, modes)) "1" else "0"
            8 -> memory[memory[currentInstruction+3].toInt()] = if(getParameter(memory, currentInstruction,0, modes) == getParameter(memory, currentInstruction,1, modes)) "1" else "0"
            99 -> halted = true
        }

        if(!jumped) currentInstruction += (parameterCounts[op] ?: error("Invalid instruction ${memory[currentInstruction]}")) + 1
    }

    fun stepUntilOutput() {
        val currentOutputLength = stdout.size
        while(stdout.size == currentOutputLength && !halted) {
            step()
        }
    }

    companion object {
        val parameterCounts = mapOf(
            1 to 3,
            2 to 3,
            3 to 1,
            4 to 1,
            5 to 2,
            6 to 2,
            7 to 3,
            8 to 3,
            99 to 0
        )
        fun getOpcode(instruction: String) = instruction.takeLast(2).toInt()
        fun getModes(instruction: String): String {
            val modeString = instruction.dropLast(2).reversed()
            return modeString.padEnd(parameterCounts.getOrElse(getOpcode(instruction)) { 0 }, '0')
        }
        fun getParameter(memory: MutableList<String>, currentInstruction: Int, index: Int, modes: String) = if(modes[index] == '0') memory[memory[currentInstruction+index+1].toInt()].toInt() else memory[currentInstruction+index+1].toInt()
    }
}

