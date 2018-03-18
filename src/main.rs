#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate rocket;
#[macro_use] extern crate diesel;
extern crate r2d2_diesel;
extern crate r2d2;
#[macro_use] extern crate dotenv_codegen;

mod db;
mod handler;

use handler::*;

fn main() {
    rocket::ignite()
        .manage(db::init_pool())
        .mount("/", routes![index, mock, files])
        .launch();
}
