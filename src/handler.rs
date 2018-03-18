use std::fs::File;
use std::io;
use std::io::BufReader;
use std::io::prelude::*;
use std::path::{Path, PathBuf};
use rocket::http::RawStr;
use rocket::response::NamedFile;

#[get("/")]
pub fn index() -> io::Result<NamedFile> {
    NamedFile::open("static/index.html")
}

// This handler is temporary.  When I redesign the DB stuff this wont be useful
#[get("/mock/<resource>")]
pub fn mock(resource: &RawStr) -> io::Result<String> {
    let resource_path = match resource.as_str() {
        "roster" | "extra" => format!("static/data/mock_{}.csv", resource),
        _ => return Ok(String::from("No such mock resource - try roster or extra"))
    };

    let file = File::open(Path::new(&resource_path))?;
    let mut buf_reader = BufReader::new(file);
    let mut contents = String::new();
    buf_reader.read_to_string(&mut contents)?;
    Ok(contents)
}

#[get("/<file..>", rank = 2)]
pub fn files(file: PathBuf) -> Option<NamedFile> {
    NamedFile::open(Path::new("static/").join(file)).ok()
}
