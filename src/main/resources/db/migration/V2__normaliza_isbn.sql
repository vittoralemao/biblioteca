-- Remove hífens e espaços dos ISBNs cadastrados antes do validador
-- e transforma ISBN vazio em NULL (ausência de ISBN).
UPDATE livros
SET isbn = NULL
WHERE trim(isbn) = '';

UPDATE livros
SET isbn = upper(replace(replace(isbn, '-', ''), ' ', ''))
WHERE isbn IS NOT NULL
  AND isbn <> upper(replace(replace(isbn, '-', ''), ' ', ''));