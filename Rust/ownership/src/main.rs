use std::fs::read;

fn main() {
    //注意：这里的字符串是String类型的，若直接赋值一个字符串的变量是&str类型的
    //&str分配在栈空间上，String类型分配在堆空间上
    let mut s = String::from("hello");
    s.push_str(", world!");
    println!("{}", s);

    let s1 = String::from("hello");
    let s2 = s1;
    //s1的值已经被转移到s2中去了，此时s1已经失效
    println!("{}", s2);

    let s1 = String::from("hello");
    let s2 = s1.clone();
    //使用clone，可以复制一份s1的值给s2
    println!("s1={}, s2={}", s1, s2);

    //因为整数，浮点数，布尔，字符，以及仅包含固定大小元素的元组长度是编译前可知的，所以默认为Copy
    let x = 5;
    let y = x;
    println!("x={}, y={}", x, y);

    let s = String::from("hello");  // s 进入作用域

    takes_ownership(s);             // s 的值移动到函数里 ...
    // println!("{}", s);  ... 所以到这里不再有效，编译器在这里会报错

    let x = 5;                 // x 进入作用域

    makes_copy(x);       // x 应该移动函数里，
                                    // 但 i32 是 Copy 的，所以在后面可继续使用 x

    let s1 = gives_ownership();         // gives_ownership 将返回值
    // 移给 s1

    let s2 = String::from("hello");    // s2 进入作用域

    let s3 = takes_and_gives_back(s2);           // s2 被移动到
                                                 // takes_and_gives_back 中,
                                                 // 它也将返回值移给 s3

    let s4 = String::from("hello");
    let len = calculate_length(&s4);

    let str = String::from("hello world");
    println!("{}", first_word(&str));

}// 这里, x 先移出了作用域，然后是 s。但因为 s 的值已被移走，
// 所以不会有特殊操作
// 这里, s3 移出作用域并被丢弃。s2 也移出作用域，但已被移走，
// 所以什么也不会发生。s1 移出作用域并被丢弃

fn takes_ownership(some_string: String) { // some_string 进入作用域
    println!("{}", some_string);
} // 这里，some_string 移出作用域并调用 `drop` 方法。占用的内存被释放

fn makes_copy(some_integer: i32) { // some_integer 进入作用域
    println!("{}", some_integer);
} // 这里，some_integer 移出作用域。不会有特殊操作

fn gives_ownership() -> String {             // gives_ownership 将返回值移动给
    // 调用它的函数
    let some_string = String::from("hello"); // some_string 进入作用域.
    some_string                              // 返回 some_string 并移出给调用的函数
}

// takes_and_gives_back 将传入字符串并返回该值
fn takes_and_gives_back(a_string: String) -> String { // a_string 进入作用域
    a_string  // 返回 a_string 并移出给调用的函数
}

fn calculate_length(s: &String) -> usize { // s 是对 String 的引用
    s.len()
}// 这里，s 离开了作用域。但因为它并不拥有引用值的所有权，
// 所以什么也不会发生

fn first_word(s: &str) -> &str {
    let bytes = s.as_bytes();

    for (i, &item) in bytes.iter().enumerate() {
        if item == b' ' {
            return &s[0..i];
        }
    }

    s
}