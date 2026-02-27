startProgram
    variables:
        int grade = 90;
        int result = 0;
    code:
        switchFor (grade)
            case 90: result = 1;
            case 80: result = 2;
            case 70: result = 3;
            other: result = 0;
        endswitchFor
        outString(result);
endProgram