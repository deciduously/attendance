use super::models::NewKid;

// TODO take a better, more concise data structure, maybe?
// When we parse in files, make this go better.
// TODO make the days options
pub fn new_kid<'a>(full_name: &'a str,
                   letter: &'a str,
                   mon: &'a str,
                   tue: &'a str,
                   wed: &'a str,
                   thu: &'a str,
                   fri: &'a str) -> NewKid<'a> {
    use super::schema::kids;

    NewKid {
        full_name: full_name,
        letter: letter,
        mon: mon,
        tue: tue,
        wed: wed,
        thu: thu,
        fri: fri,
    }

}
