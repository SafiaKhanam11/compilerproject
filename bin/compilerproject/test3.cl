startProgram
    variables:
        int num = 5;
        int fact = 1;
    code:
        loopif num > 0 holds
            fact = fact * num;
            num = num - 1;
        endloop
        outString(fact);
endProgram