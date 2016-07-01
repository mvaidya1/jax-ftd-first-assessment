import bcrypt from 'bcryptjs'

// possible destructuring to cut down on boilerplate
// const { genSalt, hash, compare } = bcrypt

/**
 * @param password [string] - the password to be hashed
 * @return [Promise] - a promise of the hashed password
 */
export function hash (password) {
  return new Promise(function executor (resolve, reject) { // function does not need to be named
    bcrypt.genSalt(function (err, salt) {
      if (err) {
        reject(err)
      } else {
        bcrypt.hash(password, salt, function (err, hashedPassword) {
          if (err) {
            reject(err)
          } else {
            resolve(hashedPassword)
          }
        })
      }
    })
  })
}

/**
 * @param plaintextPassword [string] - plain text password to compare
 * @param hashedPassword [string] - previously-hashed password to compare
 * @return [Promise] - promise containing comparison result (true if successful, false otherwise)
 */
export function compare (plaintextPassword, hashedPassword) {
  return new Promise(function executor (resolve, reject) { // does not need to be named
    bcrypt.compare(plaintextPassword, hashedPassword, function (err, successFlag) {
      if (err) {
        reject(err)
      } else {
        resolve(successFlag)
      }
    })
  })
}

export default {
  hash,
  compare
}
