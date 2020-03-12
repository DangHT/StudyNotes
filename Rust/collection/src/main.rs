use std::collections::HashMap;

fn main() {
    let v1: Vec<i32> = Vec::new();

    // 使用vec!宏创建一个新的vector
    let v2 = vec![1, 2, 3];

    let mut v3 = Vec::new();
    v3.push(5);
    v3.push(6);
    v3.push(7);
    v3.push(8);

    let v4 = vec![1, 2, 3, 4, 5];

    let third: &i32 = &v4[2];
    println!("The third element is {}", third);

    match v4.get(2) {
        Some(third) => println!("The third element is {}", third),
        None => println!("There is no third element."),
    }

    // 如何处理数组越界
    let v5 = vec![1, 2, 3];
    // let does_not_exist = &v5[100]; 这样会直接导致panic
    let does_not_exist = v5.get(100);
    println!("{}", does_not_exist == None);

    let mut v6 = vec![1, 2, 3];
    let first = &v6[0];
    // v6.push(4); 在这里向vector末尾添加新元素会直接导致panic
    println!("The first element is {}", first);
    // 为什么第一个元素的引用会关心 vector 结尾的变化？
    // 不能这么做的原因是由于 vector 的工作方式：在 vector 的结尾增加新元素时，
    // 在没有足够空间将所有所有元素依次相邻存放的情况下，可能会要求分配新内存并将老的元素拷贝到新的空间中。
    // 这时，第一个元素的引用就指向了被释放的内存。借用规则阻止程序陷入这种状况

    let v7 = vec![1, 2, 3, 4, 5, 6];
    for i in v7 {
        println!("{}", i);
    }

    let mut v8 = vec![100, 32, 57];
    for i in &mut v8 {
        *i += 50;
    }

    // vector只能存放相同类型的数据
    // 但是使用枚举可以实现vector存放多种数据
    enum SpreadsheetCell {
        Int(i32),
        Float(f64),
        Text(String),
    }
    let row = vec![
        SpreadsheetCell::Int(3),
        SpreadsheetCell::Text(String::from("blue")),
        SpreadsheetCell::Float(10.12),
    ];

    // Rust内部String是一个Vec<u8>的封装，
    // utf8中字符的长度不固定，所以String不支持索引
    // 更新字符串
    let mut s = String::from("foo");
    s.push_str("bar");
    println!("{}", s);
    s.push(char::from(127));
    println!("{}", s);

    // 使用+运算符或format!宏拼接字符串
    let s1 = String::from("hello,");
    let s2 = String::from("world!");
    let s3 = s1 + &s2; // 注意，s1被移动了，不能再继续使用
    println!("{}", s3);

    let s1 = String::from("tic");
    let s2 = String::from("tac");
    let s3 = String::from("toe");
    let s = format!("{}-{}-{}", s1, s2, s3);
    println!("{}", s);

    // 遍历字符串
    for c in "नमस्ते".chars() {
        println!("{}", c);
    }
    for b in "नमस्ते".bytes() {
        println!("{}", b);
    }

    // 新建HashMap
    let mut scores = HashMap::new();
    scores.insert(String::from("Blue"), 10);
    scores.insert(String::from("Yellow"), 50);

    // 如果队伍的名字和初始分数分别在两个 vector 中，
    // 可以使用 zip 方法来创建一个元组的 vector，其中 “Blue” 与 10 是一对，
    // 依此类推。接着就可以使用 collect 方法将这个元组 vector 转换成一个 HashMap
    let teams = vec![String::from("Blue"), String::from("Yellow")];
    let initial_scores = vec![10, 50];
    let scores: HashMap<_, _> = teams.iter().zip(initial_scores.iter()).collect();
    let score = scores.get(&teams[0]).expect("None");
    println!("score of \"Blue\" is {}", score);

    let mut scores = HashMap::new();

    scores.insert(String::from("Blue"), 10);
    scores.insert(String::from("Yellow"), 50);

    for (key, value) in &scores {
        println!("{}: {}", key, value);
    }
}
