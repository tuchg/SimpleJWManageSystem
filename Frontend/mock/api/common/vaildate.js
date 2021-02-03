const tokens = [
  'student-token',
  'admin-token',
  'teacher-token'

]

//
export function validate(token) {
  let is = false
  tokens.forEach(value => {
    // console.log(value+""+token)
    if (value === token) {
      is = true
    }
  })
  return is
}
