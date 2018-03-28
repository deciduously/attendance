#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate calamine;
extern crate rocket;
#[macro_use] extern crate diesel;
extern crate r2d2_diesel;
extern crate r2d2;

pub mod db;
pub mod handlers;
pub mod xlsx;
