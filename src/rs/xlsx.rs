use calamine::{open_workbook, Error, Xlsx, Reader};
use std::path::Path;

pub fn parse_enrollment() -> Result<(), Error> {
    let mut workbook: Xlsx<_> = open_workbook(Path::new("frontend/data/enrollment_alpha.xlsx"))?;

    if let Some(Ok(r)) = workbook.worksheet_range("Sheet1") {
        for row in r.rows() {
            println!("{:?}", row)
        }
        Ok(())
    } else {
        Err(From::from("expected at least one record but got none"))
    }
}
