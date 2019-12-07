const val code = "3,8,1001,8,10,8,105,1,0,0,21,38,55,72,93,118,199,280,361,442,99999,3,9,1001,9,2,9,1002,9,5,9,101,4,9,9,4,9,99,3,9,1002,9,3,9,1001,9,5,9,1002,9,4,9,4,9,99,3,9,101,4,9,9,1002,9,3,9,1001,9,4,9,4,9,99,3,9,1002,9,4,9,1001,9,4,9,102,5,9,9,1001,9,4,9,4,9,99,3,9,101,3,9,9,1002,9,3,9,1001,9,3,9,102,5,9,9,101,4,9,9,4,9,99,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,99"
fun main() {
    var max = 0
    for(a in 5..9) {
        for(b in 5..9) {
            if(b == a) continue
            for(c in 5..9) {
                if(c == b || c == a) continue
                for(d in 5..9) {
                    if(d == c || d == b || d == a) continue
                    for(e in 5..9) {
                        if(e == d || e == c || e == b || e == a) continue
                        println("$a $b $c $d $e")

                        val v = evaluate(arrayOf(a,b,c,d,e))
                        if(v > max) max = v

                        println(" -> $v")

                    }
                }
            }
        }
    }
    println(max)
}

fun evaluate(phases: Array<Int>): Int {

    val amp1 = IntcodeRunner(code).apply { stdin.add(phases[0].toString()); stdin.add("0") }
    val amp2 = IntcodeRunner(code).apply { stdin += phases[1].toString() }
    val amp3 = IntcodeRunner(code).apply { stdin += phases[2].toString() }
    val amp4 = IntcodeRunner(code).apply { stdin += phases[3].toString() }
    val amp5 = IntcodeRunner(code).apply { stdin += phases[4].toString() }

    while(!amp1.halted && !amp2.halted && !amp3.halted && !amp4.halted && !amp5.halted) {
        amp1.stepUntilOutput()
        amp2.stdin.add(amp1.stdout.last().toString())
        amp2.stepUntilOutput()
        amp3.stdin.add(amp2.stdout.last().toString())
        amp3.stepUntilOutput()
        amp4.stdin.add(amp3.stdout.last().toString())
        amp4.stepUntilOutput()
        amp5.stdin.add(amp4.stdout.last().toString())
        amp5.stepUntilOutput()
        amp1.stdin.add(amp5.stdout.last().toString())
    }

    return amp5.stdout.last()
}
