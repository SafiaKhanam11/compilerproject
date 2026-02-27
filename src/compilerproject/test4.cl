startProgram
    variables:
        int x = 0;
        int count = 5;
        int result = 0;
    code:
        loopif count >= 1 holds
            result = result + count;
            count = count - 1;
        endloop
        outString(result);
endProgram