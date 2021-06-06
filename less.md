// 变量

@background-color: #ffffff;



//mixins

\#circle{

  background-color: #4CAF50;

  border-radius: 100%;

 }



\#small-circle{

width: 50px;

height: 50px;

\#circle

}



//运算

@div-width: 100px;



\#left{

  width: @div-width;

  background-color: @color - 100;

}



//函数 



.box_bg(@bg:@gray){

  background:@bg;

}