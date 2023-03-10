CREATE TABLE IF NOT EXISTS public.books
(
    book_id text COLLATE pg_catalog."default" NOT NULL,
    name text COLLATE pg_catalog."default",
)

TABLESPACE pg_default;

ALTER TABLE public.books
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.orders
(
    order_id text COLLATE pg_catalog."default" NOT NULL,
    book_id text COLLATE pg_catalog."default" NOT NULL,
    product_name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (order_id),
    CONSTRAINT orders_book_id_fkey FOREIGN KEY (book_id)
        REFERENCES public.books (book_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public.orders
    OWNER to postgres;