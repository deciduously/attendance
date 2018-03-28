use db::init::DbConn;
use db::kids;
use db::models::*;
use db::schema::kids::dsl::*;
use diesel::prelude::*;
use std::fs::File;
use std::io;
use std::io::BufReader;
use std::io::prelude::*;
use std::path::{Path, PathBuf};
use rocket::http::RawStr;
use rocket::response::NamedFile;

#[get("/")]
pub fn index() -> io::Result<NamedFile> {
    NamedFile::open("frontend/index.html")
}

// This handler is temporary.  When I redesign the DB stuff this wont be useful
#[get("/mock/<resource>")]
pub fn mock(resource: &RawStr) -> io::Result<String> {
    let resource_path = match resource.as_str() {
        "roster" | "extra" => format!("frontend/data/mock_{}.csv", resource),
        _ => return Ok(String::from("No such mock resource - try roster or extra"))
    };

    let file = File::open(Path::new(&resource_path))?;
    let mut buf_reader = BufReader::new(file);
    let mut contents = String::new();
    buf_reader.read_to_string(&mut contents)?;
    Ok(contents)
}

// TODO merge roster/extra handlers
#[post("/data/roster", format = "text/plain", data = "<roster>")]
pub fn upload(roster: String) -> io::Result<String> {
    println!("{}", roster);
    Ok(roster)
}

//#[get("/kids")]
//pub fn get_kids(conn: DbConn) -> QueryResult<Json<Vec<Kid>>> {
//    all_kids.order(kids::id.desc())
//        .load::<Kid>(&*conn)
//        .map(|kids| Json(kids))
//}


// TODO implement FromData for a Kid
// Figure out how we're handling upload
// If you have a csv, have a SEPARATE handler
// In fact, no POST for Kid - POST for the roster
// Which is then parsed and passed to the DB internally.
// Only expose GET for individual kids/kidrecords
//#[post("/kid", data = "<name>")]
//fn new_kid(conn: &DbConn, name: &RawStr) -> Option<Kid> {
//    insert_into(kids::table)
//        .values(kids::new_kid(name, "A", "8-4", "8-4", "8-4", "8-4", "8-4"))
//        .get_result(conn)
//        .expect("Error saving new kid")
//}

// TODO decide how to handle static/ - should it all go in frontend like this?

#[get("/<file..>", rank = 2)]
pub fn files(file: PathBuf) -> Option<NamedFile> {
    NamedFile::open(Path::new("frontend/").join(file)).ok()
}
