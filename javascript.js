var i=10;
i=i++;
function fib(j){
    if(j==1){
        return 1;
    }
    if(j==0){
        return 0;
    }
return fib(j-2)+fib(j-1);
}
i=fib(i);