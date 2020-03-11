enum IpAddr{
    V4(u8, u8, u8, u8),
    V6(String),
}

#[derive(Debug)]
enum UsState {
    Alabama,
    Alaska,
}

enum Coin {
    Penny,
    Nickel,
    Dime,
    Quarter(UsState)
}

fn main() {
    let home = IpAddr::V4(127, 0, 0, 1);
    let loopback = IpAddr::V6(String::from("::1"));

    let coin = Coin::Quarter(UsState::Alaska);
    println!("coin is {}", value_in_cents(coin));

    let five = Some(5);
    let six = plus_one(five);
    let none = plus_one(None);
    println!("five is {:?}", five);
    println!("six is {:?}", six);

    // let mut count = 0;
    // let coin = Coin::Quarter(UsState::Alabama);
    // match coin {
    //     Coin::Quarter(state) => println!("State quarter from {:?}!", state),
    //     _ => count + 1,
    // }

    // 以上一段代码可以使用if let表达式简化
    let mut count = 0;
    let coin = Coin::Quarter(UsState::Alabama);
    if let Coin::Quarter(state) = coin {
        println!("State quarter is {:?}!", state);
    } else {
        count += 1;
    }
}

fn value_in_cents(coin: Coin) -> u8 {
    match coin {
        Coin::Penny => 1,
        Coin::Nickel => 5,
        Coin::Dime => 10,
        Coin::Quarter(state) => {
            println!("State quarter from {:?}!", state);
            25
        },
    }
}

fn plus_one(x: Option<i32>) -> Option<i32> {
    match x {
        None => None,
        Some(i) => Some(i + 1),
    }
}
