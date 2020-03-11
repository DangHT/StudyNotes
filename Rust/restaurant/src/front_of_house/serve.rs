fn take_order() {}

fn server_order() {}

mod back_of_house {
    fn fix_incorrect_order() {
        cook_order();
        super::server_order();
    }

    fn cook_order() {}
}

fn take_payment() {}

