fn main() {
    //声明一个可变变量
    let mut x = 5;
    println!("The value of x is: {}", x);
    x = 6;
    println!("The value of x is: {}", x);

    //声明一个常量
    const MAX_POINTS: u32 = 100_000;
    println!("MAX_POINTS = {}", MAX_POINTS);

    //Shadowing
    let y = 5;
    let y = y + 1;
    let y = y * 2;
    println!("The value of y is: {}", y);

    //Shadowing可以改变变量类型
    let spaces = "     ";
    let spaces = spaces.len();
    println!("spaces.len() = {}", spaces);

    //isize或usize类型整数（由机器位数决定整型位数）
    let a: isize = 64;
    println!("a(arch) is: {}", a);

    //float 共两种类型：f32和f64，默认为f64
    let f: f32 = 1.5;
    println!("Float number: {}", f);

    //character
    let c = 'z';
    let z = 'ℤ';
    let heart_eyed_cat = '😻';
    println!("Characters: {}, {}, {}", c, z, heart_eyed_cat);

    //Tuple tuple固定长度，一但声明便不可更改; tuple中各个元素可以是不同的类型
    let tup = (500, 6.4, "😬");
    let (x, y, z) = tup;
    println!("x:{}, y:{}, z:{}", x, y, z);
    println!("tup.0:{}, tup.1:{}, tup.2:{}", tup.0, tup.1, tup.2);

    //Array array同样固定长度; 所有元素必须类型一致
    let array = [1, 2, 3, 4, 5];
    let months = ["January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"];
    let array = [3; 5]; //array = [3, 3, 3, 3, 3]

    //定义函数
    println!("{}", add_nums(3, 2));

    let x = 5;
    let y = {
        let x = 3;
        //注意！表达式不可以加分号，如果加了分号就变成了语句了
        //(https://doc.rust-lang.org/book/ch03-03-how-functions-work.html#function-bodies-contain-statements-and-expressions)
        x + 1
    };
    println!("x:{}, y:{}", x, y);

    //Control Flow
    if x == 5 {
        println!("x == 5");
    } else {
        println!("x != 5");
    }

    let mut counter = 1;
    loop {
        if counter == 10 { break; }
        print!("{} ", counter);
        counter += 1;
    }

    while counter > 0 {
        print!("{} ", counter);
        counter -= 1;
    }

    println!();

    let array = [1, 2, 3, 4, 5, 6, 7, 8, 9];
    for i in array.iter() {
        print!("{} ", i);
    }

    println!();

    for word in (1..10).rev() {
        print!("{} ", word);
    }
}

//在Rust中，函数的返回值可以是函数体中最后的一个表达式
fn add_nums(a: i32, b: i32) -> i32 {
    // return a + b;
    //写成表达式形式
    a + b
}
