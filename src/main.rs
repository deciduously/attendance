#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate rocket;

use std::fs::File;
use std::io;
use std::io::BufReader;
use std::io::prelude::*;
use std::path::{Path, PathBuf};

use rocket::response::NamedFile;

#[get("/")]
fn index() -> io::Result<NamedFile> {
    NamedFile::open("static/index.html")
}

// These handlers are temporary.  When I redesign the DB stuff this wont be useful
#[get("/mock/roster")]
fn mock_roster() -> io::Result<String> {
    let file = File::open(Path::new("static/data/mock_roster.csv"))?;
    let mut buf_reader = BufReader::new(file);
    let mut contents = String::new();
    buf_reader.read_to_string(&mut contents)?;
    Ok(contents)
}

#[get("/mock/extra")]
fn mock_extra() -> io::Result<String> {
    let file = File::open(Path::new("static/data/mock_extra.csv"))?;
    let mut buf_reader = BufReader::new(file);
    let mut contents = String::new();
    buf_reader.read_to_string(&mut contents)?;
    Ok(contents)
}

#[get("/<file..>")]
fn files(file: PathBuf) -> Option<NamedFile> {
    NamedFile::open(Path::new("static/").join(file)).ok()
}

fn main() {
    rocket::ignite().mount("/", routes![index, mock_roster, mock_extra, files]).launch();
}
