pub struct Breakfast {
    pub toast: String,
    seasonal_fruit: String,
}

//枚举成员默认是公有的
pub enum Appetizer {
    Soup,
    Salad,
}

impl Breakfast {
    pub fn summer(toast: &str) -> Breakfast {
        Breakfast {
            toast: String::from(toast),
            seasonal_fruit: String::from("peaches"),
        }
    }
}